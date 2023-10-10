package org.example.domain;

import org.example.data.AppUserRepository;
import org.example.domain.ResultType;
import org.example.domain.Result;
import org.example.models.AppUser;
import org.example.util.RandomStringGenerator;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AppUserService implements UserDetailsService {

    private final AppUserRepository repository;

    private final PasswordEncoder encoder;

    private final RandomStringGenerator randomStringGenerator;

    private final static List<String> DEFAULT_ROLES = List.of("APPLICANT");

    public AppUserService(AppUserRepository repository, PasswordEncoder encoder, RandomStringGenerator randomStringGenerator) {
        this.repository = repository;
        this.encoder = encoder;
        this.randomStringGenerator = randomStringGenerator;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        AppUser appUser = repository.findByUsername(username);

        if (appUser == null || !appUser.isEnabled()) {
            throw new UsernameNotFoundException(username + " not found");
        }

        return appUser;
    }

    public Result<AppUser> create(String username) {
        String externalId = randomStringGenerator.generateRandomString();
        String account_setup_token = randomStringGenerator.generateRandomString();
        AppUser appUser = new AppUser(username, externalId, account_setup_token, DEFAULT_ROLES);
        Result<AppUser> result = new Result<>();

        try {
            appUser = repository.create(appUser);
            result.setPayload(appUser);
        } catch (DuplicateKeyException e) {
            result.addErrorMessage("The provided username already exists", ResultType.INVALID);
        }

        return result;
    }

    private Result<AppUser> validateForUpdate(String username, String password) {
        Result<AppUser> result = new Result<>();
        if (username == null || username.isBlank()) {
            result.addErrorMessage("username is required", ResultType.INVALID);
            return result;
        }

        if (password == null) {
            result.addErrorMessage("password is required", ResultType.INVALID);
            return result;
        }

        if (username.length() > 50) {
            result.addErrorMessage("username must be less than 50 characters", ResultType.INVALID);
        }

        if (!isValidPassword(password)) {
            result.addErrorMessage("password must be at least 8 characters", ResultType.INVALID);
        }

        return result;
    }

    private boolean isValidPassword(String password) {
        return password.length() >= 8;
    }
}

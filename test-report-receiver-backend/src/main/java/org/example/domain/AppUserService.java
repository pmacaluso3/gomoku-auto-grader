package org.example.domain;

import org.example.data.AppUserRepository;
import org.example.models.AppUser;
import org.example.util.RandomStringGenerator;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    public List<AppUser> findAllApplicants() {
        return repository.findAllApplicants();
    }

    public Result<AppUser> create(String username) {
        Result<AppUser> result = new Result<>();
        if (username.isBlank()) {
            result.addErrorMessage("Username cannot be blank", ResultType.INVALID);
            return result;
        }

        String account_setup_token = randomStringGenerator.generateRandomString();
        AppUser appUser = new AppUser(username, account_setup_token, DEFAULT_ROLES);

        try {
            appUser = repository.create(appUser);
            result.setPayload(appUser);
        } catch (DuplicateKeyException e) {
            result.addErrorMessage(String.format("Username %s already exists", appUser.getUsername()), ResultType.INVALID);
        }

        return result;
    }

    public Result<AppUser> accountSetup(AppUser appUser) {
        Result<AppUser> result = validateForAccountSetup(appUser);
        if (result.isSuccess()) {
            appUser.setPassword(encoder.encode(appUser.getPassword()));
            boolean setupOutcome = repository.accountSetup(appUser);
            if (!setupOutcome) {
                result.addErrorMessage("Could not find user with accountSetupToken %s", ResultType.INVALID, appUser.getAccountSetupToken());
            }
        }
        return result;
    }

    public Map<String, List<String>> createBulk(List<String> usernames) {
        Map<String, List<String>> output = new HashMap<>();
        output.put("successes", new ArrayList<>());
        output.put("failures", new ArrayList<>());
        for (String username : usernames) {
            Result<AppUser> result = create(username);
            if (result.isSuccess()) {
                output.get("successes").add(String.format("%s created successfully", username));
            } else {
                output.get("failures").add(result.getErrorMessages().get(0));
            }
        }
        return output;
    }

    private Result<AppUser> validateForAccountSetup(AppUser appUser) {
        Result<AppUser> result = new Result<>();

        if (appUser.getPassword() == null) {
            result.addErrorMessage("password is required", ResultType.INVALID);
            return result;
        }

        if (appUser.getPassword().length() < 8) {
            result.addErrorMessage("password must be at least 8 characters", ResultType.INVALID);
        }

        if (appUser.getFirstName() == null || appUser.getFirstName().isBlank()) {
            result.addErrorMessage("first name is required", ResultType.INVALID);
        }

        if (appUser.getLastName() == null || appUser.getLastName().isBlank()) {
            result.addErrorMessage("first name is required", ResultType.INVALID);
        }

        return result;
    }
}

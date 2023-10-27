package org.example.data;

import org.example.models.AppUser;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface AppUserRepository {
    @Transactional
    AppUser findByUsername(String username);

    List<AppUser> findAllApplicants();

    List<AppUser> findWhereIdInList(List<Integer> ids);

    @Transactional
    AppUser create(AppUser user);

    @Transactional
    void update(AppUser user);

    boolean accountSetup(AppUser appUser);
}

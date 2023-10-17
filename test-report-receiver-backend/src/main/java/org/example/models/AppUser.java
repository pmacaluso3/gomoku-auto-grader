package org.example.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class AppUser implements UserDetails {

    private int appUserId;
    private String username;
    private String password;
    private boolean enabled;
    private Collection<GrantedAuthority> authorities;
    private String externalId;
    private String accountSetupToken;
    private String firstName;
    private String lastName;
    private boolean hasBeenSetup;

    public AppUser(int appUserId,
                   String firstName,
                   String lastName,
                   String externalId,
                   String accountSetupToken,
                   boolean hasBeenSetup,
                   String username,
                   String password,
                   boolean enabled,
                   List<String> roles) {
        this.appUserId = appUserId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.externalId = externalId;
        this.accountSetupToken = accountSetupToken;
        this.hasBeenSetup = hasBeenSetup;
        this.username = username;
        this.password = password;
        this.enabled = enabled;
        this.authorities = convertRolesToAuthorities(roles);
    }

    // for initial creation by admin
    public AppUser(String username, String externalId, String accountSetupToken, List<String> roles) {
        this.username = username;
        this.externalId = externalId;
        this.accountSetupToken = accountSetupToken;
        this.authorities = convertRolesToAuthorities(roles);
    }

    public AppUser(){}

    private static Collection<GrantedAuthority> convertRolesToAuthorities(List<String> roles) {
        return roles.stream()
                .map(r -> new SimpleGrantedAuthority(r))
                .collect(Collectors.toList());
    }

    @Override
    public Collection<GrantedAuthority> getAuthorities() {
        return new ArrayList<>(authorities);
    }

    @JsonIgnore
    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }

    public int getAppUserId() {
        return appUserId;
    }

    public void setAppUserId(int appUserId) {
        this.appUserId = appUserId;
    }

    public String getExternalId() {
        return externalId;
    }

    public String getAccountSetupToken() {
        return accountSetupToken;
    }

    public void setAccountSetupToken(String accountSetupToken) {
        this.accountSetupToken = accountSetupToken;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    @JsonProperty
    public void setPassword(String password) {
        this.password = password;
    }
}

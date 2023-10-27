package org.example.controllers;

import org.example.domain.AppUserService;
import org.example.domain.EmailService;
import org.example.domain.Result;
import org.example.models.AppUser;
import org.example.security.JwtConverter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/users")
public class AppUserController {

    private final AuthenticationManager authenticationManager;
    private final JwtConverter converter;

    private final AppUserService service;
    private final EmailService emailService;

    public AppUserController(AuthenticationManager authenticationManager, JwtConverter converter, AppUserService service, EmailService emailService) {
        this.authenticationManager = authenticationManager;
        this.converter = converter;
        this.service = service;
        this.emailService = emailService;
    }

    @GetMapping("/applicants")
    public List<AppUser> getAllApplicants() {
        return service.findAllApplicants();
    }

    @PostMapping("/authenticate")
    public ResponseEntity<Map<String, String>> authenticate(@RequestBody Map<String, String> credentials) {

        UsernamePasswordAuthenticationToken authToken =
                new UsernamePasswordAuthenticationToken(credentials.get("username"), credentials.get("password"));

        try {
            Authentication authentication = authenticationManager.authenticate(authToken);

            if (authentication.isAuthenticated()) {
                String jwtToken = converter.getTokenFromUser((UserDetails) authentication.getPrincipal());

                HashMap<String, String> map = new HashMap<>();
                map.put("jwt_token", jwtToken);

                return new ResponseEntity<>(map, HttpStatus.OK);
            }

        } catch (AuthenticationException ex) {
            System.out.println(ex);
        }

        return new ResponseEntity<>(HttpStatus.FORBIDDEN);
    }

    @PostMapping
    public ResponseEntity<Object> create(@RequestBody Map<String, String> usernames) {
        List<String> listOfUsernames = List.of(usernames.get("usernames").split(","));
        Map<String, List<String>> result = service.createBulk(listOfUsernames);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PutMapping("/account_setup")
    public ResponseEntity<Object> accountSetup(@RequestBody AppUser appUser) {
        Result<AppUser> result = service.accountSetup(appUser);
        return ControllerHelper.convertResultToHttpResponse(result);
    }

    @PostMapping("/send_setup_emails/{ids}")
    public ResponseEntity<Object> sendSetupEmails(@PathVariable List<Integer> ids) {
        try {
            emailService.sendSetupEmailsToUsers(ids);
            return new ResponseEntity<>("ok", HttpStatus.OK);
        } catch (Exception ex) {
            return new ResponseEntity<>(List.of(ex.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }
}

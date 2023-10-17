package org.example.controllers;


import org.example.domain.AppUserService;
import org.example.domain.Result;
import org.example.domain.SubmissionService;
import org.example.models.AppUser;
import org.example.models.Submission;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/submissions")
public class SubmissionsController {

    private final SubmissionService service;

    private final AppUserService appUserService;

    public SubmissionsController(SubmissionService service, AppUserService appUserService) {
        this.service = service;
        this.appUserService = appUserService;
    }

    @GetMapping("/all")
    public List<Submission> findAll() {
        return service.findAll();
    }

    @GetMapping("/mine")
    public ResponseEntity<Object> findByAuthorizedApplicant() {
        Result<List<Submission>> result = service.findByApplicantUsername(getAuthorizedUsername());
        return ControllerHelper.convertResultToHttpResponse(result);
    }

//    @PostMapping
//    public ResponseEntity<Object> create(@RequestParam("file") MultipartFile file) {
////        submission.setAppUserId(getAuthorizedUserId());
////        Result<Submission> result = service.create(submission);
////        return ControllerHelper.convertResultToHttpResponse(result);
//        return null;
//    }

    private String getAuthorizedUsername() {
        return (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }

    private int getAuthorizedUserId() {
        AppUser user = (AppUser) appUserService.loadUserByUsername(getAuthorizedUsername());
        return user.getAppUserId();
    }
}

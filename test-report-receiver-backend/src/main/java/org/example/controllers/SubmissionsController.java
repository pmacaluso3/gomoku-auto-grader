package org.example.controllers;


import org.example.domain.AppUserService;
import org.example.domain.Result;
import org.example.domain.SubmissionService;
import org.example.models.AppUser;
import org.example.models.Submission;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Blob;
import java.sql.SQLException;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

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

    @GetMapping("/zip_files/{ids}")
    public ResponseEntity<Object> getZipFilesByIdList(@PathVariable List<Integer> ids) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        service.getZipFileFromIds(ids, baos);
        ByteArrayResource resource = new ByteArrayResource(baos.toByteArray());

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=multiple_zips.zip");
        headers.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_OCTET_STREAM_VALUE);

        return ResponseEntity.ok()
                .headers(headers)
                .body(resource);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> findById(@PathVariable int id) {
        Result<Submission> result = service.findById(id);
        return ControllerHelper.convertResultToHttpResponse(result);
    }

    @PostMapping
    public ResponseEntity<Object> create(@RequestPart("zipFile") MultipartFile zipFile) throws IOException, SQLException {
        Submission submission = new Submission(getAuthorizedUserId(), zipFile);
        Result<Submission> result = service.create(submission);
        return ControllerHelper.convertResultToHttpResponse(result);
    }

    @PutMapping("/mark_graded")
    public ResponseEntity<Object> markGraded(@RequestBody Submission submission) {
        Result<Submission> result = service.markGraded(submission);
        return ControllerHelper.convertResultToHttpResponse(result);
    }

    private String getAuthorizedUsername() {
        return (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }

    private int getAuthorizedUserId() {
        AppUser user = (AppUser) appUserService.loadUserByUsername(getAuthorizedUsername());
        return user.getAppUserId();
    }
}

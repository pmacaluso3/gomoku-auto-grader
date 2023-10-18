package org.example.controllers;

import org.example.domain.GradingBatchService;
import org.example.domain.Result;
import org.example.models.GradingBatch;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/grading_batches")
public class GradingBatchController {
    private final GradingBatchService service;

    public GradingBatchController(GradingBatchService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<Object> findAll() {
        List<GradingBatch> gradingBatches = service.findAll();
        return new ResponseEntity<>(gradingBatches, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> findById(@PathVariable int id) {
        Result<GradingBatch> result = service.findById(id);
        return ControllerHelper.convertResultToHttpResponse(result);
    }

    @PostMapping
    public ResponseEntity<Object> create() {
        Result<GradingBatch> result = service.create(new GradingBatch());
        return ControllerHelper.convertResultToHttpResponse(result);
    }
}

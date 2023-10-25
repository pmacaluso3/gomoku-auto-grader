package org.example.controllers;

import org.example.domain.Result;
import org.example.domain.TestCaseOutcomeService;
import org.example.models.TestCaseOutcome;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/test_case_outcomes")
public class TestCaseOutcomeController {

    private final TestCaseOutcomeService service;

    public TestCaseOutcomeController(TestCaseOutcomeService service) {
        this.service = service;
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> findById(@PathVariable int id) {
        Result<TestCaseOutcome> result = service.findById(id);
        return ControllerHelper.convertResultToHttpResponse(result);
    }

    @PostMapping
    public ResponseEntity<Object> create(@RequestBody TestCaseOutcome testCaseOutcome) {
        Result<TestCaseOutcome> result = service.create(testCaseOutcome);
        return ControllerHelper.convertResultToHttpResponse(result);
    }

    @PutMapping
    public ResponseEntity<Object> update(@RequestBody TestCaseOutcome testCaseOutcome) {
        Result<TestCaseOutcome> result = service.update(testCaseOutcome);
        return ControllerHelper.convertResultToHttpResponse(result);
    }
}

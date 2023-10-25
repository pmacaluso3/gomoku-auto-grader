package org.example.domain;

import org.example.data.TestCaseOutcomeRepository;
import org.example.models.TestCaseOutcome;
import org.springframework.stereotype.Service;

@Service
public class TestCaseOutcomeService {

    private final TestCaseOutcomeRepository repository;

    public TestCaseOutcomeService(TestCaseOutcomeRepository repository) {
        this.repository = repository;
    }

    public Result<TestCaseOutcome> create(TestCaseOutcome testCaseOutcome) {
        Result<TestCaseOutcome> result = validate(testCaseOutcome);

        if (result.isSuccess()) {
            result.setPayload(repository.create(testCaseOutcome));
        }

        return result;
    }

    public Result<TestCaseOutcome> update(TestCaseOutcome testCaseOutcome) {
        Result<TestCaseOutcome> result = new Result<>();

        boolean updateResult = repository.update(testCaseOutcome);
        if (updateResult) {
            result.setPayload(testCaseOutcome);
        } else {
            result.addErrorMessage("Could not find record to update", ResultType.NOT_FOUND);
        }

        return result;
    }

    private Result<TestCaseOutcome> validate(TestCaseOutcome testCaseOutcome) {
        Result<TestCaseOutcome> result = new Result<>();

        if (testCaseOutcome.getTestName() == null || testCaseOutcome.getTestName().isBlank()) {
            result.addErrorMessage("Name must be present", ResultType.INVALID);
        }

        if (testCaseOutcome.getSubmissionId() == 0) {
            result.addErrorMessage("Must belong to a submission", ResultType.INVALID);
        }

        return result;
    }

}

package org.example.domain;

import org.example.data.GradingBatchRepository;
import org.example.data.SubmissionRepository;
import org.example.models.GradingBatch;
import org.example.models.Submission;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GradingBatchService {
    private final GradingBatchRepository repository;

    private final SubmissionRepository submissionRepository;

    public GradingBatchService(GradingBatchRepository repository, SubmissionRepository submissionRepository) {
        this.repository = repository;
        this.submissionRepository = submissionRepository;
    }

    public List<GradingBatch> findAll() {
        return repository.findAll();
    }

    public Result<GradingBatch> findById(int id) {
        Result<GradingBatch> result = new Result<>();
        GradingBatch gradingBatch = repository.findById(id);
        if (gradingBatch == null) {
            result.addErrorMessage("Could not find gradingBatch", ResultType.NOT_FOUND);
        } else {
            List<Submission> submissions = submissionRepository.findByGradingBatchId(id);
            gradingBatch.setSubmissions(submissions);
            result.setPayload(gradingBatch);
        }
        return result;
    }

    public Result<GradingBatch> create(GradingBatch gradingBatch) {
        Result<GradingBatch> result = new Result<>();
        GradingBatch createdBatch = repository.create(gradingBatch);
        if (gradingBatch == null) {
            result.addErrorMessage("Could not create gradingBatch", ResultType.INVALID);
        } else {
            result.setPayload(gradingBatch);
        }
        return result;
    }
}

package org.example.domain;

import org.example.data.AppUserRepository;
import org.example.data.SubmissionRepository;
import org.example.models.AppUser;
import org.example.models.Submission;
import org.springframework.stereotype.Component;

import java.sql.PreparedStatement;
import java.util.List;

@Component
public class SubmissionService {

    private final SubmissionRepository repository;

    private final AppUserRepository appUserRepository;

    public SubmissionService(SubmissionRepository repository, AppUserRepository appUserRepository) {
        this.repository = repository;
        this.appUserRepository = appUserRepository;
    }

    public List<Submission> findAll() {
        return repository.findAll();
    }

    public Result<List<Submission>> findByApplicantUsername(String username) {
        Result<List<Submission>> result = new Result<>();
        AppUser user = appUserRepository.findByUsername(username);
        if (user == null) {
            result.addErrorMessage("Could not find that applicant", ResultType.NOT_FOUND);
        } else {
            List<Submission> queryResult = repository.findByApplicantUsername(username);
            result.setPayload(queryResult);
        }
        return result;
    }

    public Result<Submission> create(Submission submission) {
        Result<Submission> result = validate(submission);
        if (result.isSuccess()) {
            result.setPayload(repository.create(submission));
        }
        return result;
    }

    public Result<Submission> markGraded(Submission submission) {
        Result<Submission> result = new Result<>();
        Submission udpatedSubmission = repository.markGraded(submission);
        if (udpatedSubmission == null) {
            result.addErrorMessage("Could not find submission to update", ResultType.NOT_FOUND);
        } else {
            result.setPayload(udpatedSubmission);
        }
        return result;
    }

    private Result<Submission> validate(Submission submission) {
        Result<Submission> result = new Result<>();
        if (submission.getZipFile() == null) {
            result.addErrorMessage("zipFile cannot be blank", ResultType.INVALID);
        }
        return result;
    }

}

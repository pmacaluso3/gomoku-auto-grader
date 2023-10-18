package org.example.data;

import org.example.models.Submission;

import java.util.List;

public interface SubmissionRepository {
    public List<Submission> findAll();

    public List<Submission> findByApplicantUsername(String username);

    public List<Submission> findByGradingBatchId(int id);

    public Submission create(Submission submission);

    public boolean update(Submission submission);
}

package org.example.models;

import java.time.LocalDateTime;
import java.util.List;

public class GradingBatch {
    private int gradingBatchId;

    private List<Submission> submissions;

    private LocalDateTime createdAt;

    public GradingBatch() {}

    public int getGradingBatchId() {
        return gradingBatchId;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public void setGradingBatchId(int gradingBatchId) {
        this.gradingBatchId = gradingBatchId;
    }

    public List<Submission> getSubmissions() {
        return submissions;
    }

    public void setSubmissions(List<Submission> submissions) {
        this.submissions = submissions;
    }
}

package org.example.models;

import java.util.List;

public class GradingBatch {
    private int gradingBatchId;

    private List<Submission> submissions;

    public GradingBatch(int gradingBatchId, List<Submission> submissions) {
        this.gradingBatchId = gradingBatchId;
        this.submissions = submissions;
    }

    public GradingBatch() {}

    public int getGradingBatchId() {
        return gradingBatchId;
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

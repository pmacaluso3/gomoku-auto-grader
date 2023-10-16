package org.example.models;

import java.sql.Blob;
import java.sql.Timestamp;

public class Submission {
    private int submissionId;
    private int appUserId;
    private Blob zipFile;
    private int gradingBatchId;
    private Timestamp createdAt;
    private Timestamp gradedAt;

    public Submission(int submissionId, int appUserId, Blob zipFile, int gradingBatchId, Timestamp createdAt, Timestamp gradedAt) {
        this.submissionId = submissionId;
        this.appUserId = appUserId;
        this.zipFile = zipFile;
        this.gradingBatchId = gradingBatchId;
        this.createdAt = createdAt;
        this.gradedAt = gradedAt;
    }

    public Submission() {};

    public int getSubmissionId() {
        return submissionId;
    }

    public void setSubmissionId(int submissionId) {
        this.submissionId = submissionId;
    }

    public int getAppUserId() {
        return appUserId;
    }

    public void setAppUserId(int appUserId) {
        this.appUserId = appUserId;
    }

    public Blob getZipFile() {
        return zipFile;
    }

    public void setZipFile(Blob zipFile) {
        this.zipFile = zipFile;
    }

    public int getGradingBatchId() {
        return gradingBatchId;
    }

    public void setGradingBatchId(int gradingBatchId) {
        this.gradingBatchId = gradingBatchId;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    public Timestamp getGradedAt() {
        return gradedAt;
    }

    public void setGradedAt(Timestamp gradedAt) {
        this.gradedAt = gradedAt;
    }
}

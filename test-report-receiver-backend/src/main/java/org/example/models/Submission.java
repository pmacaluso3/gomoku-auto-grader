package org.example.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.web.multipart.MultipartFile;

import javax.sql.rowset.serial.SerialException;
import java.io.IOException;
import java.sql.Blob;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class Submission {
    private int submissionId;
    private int appUserId;
    private Blob zipFile;
    private int gradingBatchId;
    private boolean archived;

    public boolean isArchived() {
        return archived;
    }

    public void setArchived(boolean archived) {
        this.archived = archived;
    }

    private Timestamp createdAt;
    private Timestamp gradedAt;
    private List<TestCaseOutcome> testCaseOutcomes = new ArrayList<>();
    private AppUser appUser;
    public Submission(int appUserId, MultipartFile zipFile) {
        this.appUserId = appUserId;
        byte[] bytes = new byte[0];
        try {
            bytes = zipFile.getBytes();
            Blob blob = new javax.sql.rowset.serial.SerialBlob(bytes);
            this.zipFile = blob;
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (SerialException e) {
            throw new RuntimeException(e);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
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

    @JsonIgnore
    public Blob getZipFile() {
        return zipFile;
    }

    @JsonProperty
    public void setZipFile(Blob zipFile) {
        this.zipFile = zipFile;
    }

    public int getGradingBatchId() {
        return gradingBatchId;
    }

    public List<TestCaseOutcome> getTestCaseOutcomes() {
        return testCaseOutcomes;
    }

    public void setTestCaseOutcomes(List<TestCaseOutcome> testCaseOutcomes) {
        this.testCaseOutcomes = testCaseOutcomes;
    }

    public void addTestCaseOutcome(TestCaseOutcome testCaseOutcome) {
        this.testCaseOutcomes.add(testCaseOutcome);
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

    public AppUser getAppUser() {
        return appUser;
    }

    public void setAppUser(AppUser appUser) {
        this.appUser = appUser;
    }
}

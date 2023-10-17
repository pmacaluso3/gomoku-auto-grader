package org.example.models;

public class TestCaseOutcome {
    private int testCaseOutcomeId;
    private int submissionId;
    private boolean success;
    private boolean hasBeenManuallyEdited;
    private String description;
    private String boardState;

    public boolean hasBeenManuallyEdited() {
        return hasBeenManuallyEdited;
    }

    public void setHasBeenManuallyEdited(boolean hasBeenManuallyEdited) {
        this.hasBeenManuallyEdited = hasBeenManuallyEdited;
    }

    public TestCaseOutcome(int testCaseOutcomeId,
                           int submissionId,
                           boolean success,
                           boolean hasBeenManuallyEdited,
                           String description,
                           String boardState) {
        this.testCaseOutcomeId = testCaseOutcomeId;
        this.submissionId = submissionId;
        this.success = success;
        this.hasBeenManuallyEdited = hasBeenManuallyEdited;
        this.description = description;
        this.boardState = boardState;
    }

    public TestCaseOutcome() {}

    public int getTestCaseOutcomeId() {
        return testCaseOutcomeId;
    }

    public void setTestCaseOutcomeId(int testCaseOutcomeId) {
        this.testCaseOutcomeId = testCaseOutcomeId;
    }

    public int getSubmissionId() {
        return submissionId;
    }

    public void setSubmissionId(int submissionId) {
        this.submissionId = submissionId;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getBoardState() {
        return boardState;
    }

    public void setBoardState(String boardState) {
        this.boardState = boardState;
    }
}

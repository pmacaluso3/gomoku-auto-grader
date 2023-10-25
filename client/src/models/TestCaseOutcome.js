import { Link } from "react-router-dom";
import Record from "./Record";

export default class TestCaseOutcome extends Record {
    constructor({ submissionId, testName, testCaseOutcomeId, success, hasBeenManuallyEdited, description, boardState }) {
        super()
        this.submissionId = submissionId
        this.testName = testName
        this.testCaseOutcomeId = testCaseOutcomeId
        this.success = success
        this.hasBeenManuallyEdited = hasBeenManuallyEdited
        this.description = description
        this.boardState = boardState
    }

    passed() {
        return this.success ? "YES" : "NO"
    }

    manuallyEdited() {
        return this.hasBeenManuallyEdited ? "YES" : "NO"
    }

    board() {
        return this.boardState && {
            isHtml: true,
            value: this.boardState.replaceAll("\r\n", "<br/>")
        }
    }

    markAsSuccessful() {
        return <Link to={`/testCaseOutcomes/${this.testCaseOutcomeId}`}>Modify?</Link>
    }
}
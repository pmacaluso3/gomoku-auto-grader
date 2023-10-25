import Record from "./Record"

export default class Submission extends Record {
    constructor({ submissionId, appUserId, gradingBatchId, createdAt, gradedAt, testCaseOutcomes }) {
        super()
        this.submissionId = submissionId
        this.appUserId = appUserId
        this.gradingBatchId = gradingBatchId
        this.createdAt = createdAt
        this.gradedAt = gradedAt
        this.testCaseOutcomes = testCaseOutcomes
    }

    numberOfPassingTests() {
        return this.testCaseOutcomes.filter(tco => tco.success && tco.boardState === null).length
    }

    numberOfFailingTests() {
        return this.testCaseOutcomes.filter(tco => !tco.success && tco.boardState === null).length
    } 

    gradingBatch() {
        return this.gradedAt === null ? null : this.gradingBatchId
    }
}
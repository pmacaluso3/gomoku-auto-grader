import Record from "./Record";

export default class GradingBatch extends Record {
    constructor({ gradingBatchId, createdAt }) {
        super()
        this.gradingBatchId = gradingBatchId
        this.createdAt = createdAt
    }
}
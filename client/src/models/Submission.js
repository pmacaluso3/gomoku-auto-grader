import { Link } from "react-router-dom"

import Record from "./Record"

export default class Submission extends Record {
    constructor({ submissionId, appUserId, gradingBatchId, createdAt, gradedAt, testCaseOutcomes, appUser }) {
        super()
        this.submissionId = submissionId
        this.appUserId = appUserId
        this.appUser = appUser        
        this.gradingBatchId = gradingBatchId
        this.createdAt = createdAt
        this.gradedAt = gradedAt
        this.testCaseOutcomes = testCaseOutcomes
    }

    numberOfPassingTests() {
        if (this.gradedAt === null) return null
        return this.testCaseOutcomes.filter(tco => tco.success).length
    }

    numberOfFailingTests() {
        if (this.gradedAt === null) return null
        return this.testCaseOutcomes.filter(tco => !tco.success).length
    } 

    gradingBatch() {
        return this.gradedAt === null ? null : this.gradingBatchId
    }

    userInfo() {
        return this.appUser && `${this.appUser.firstName} ${this.appUser.lastName} (username: ${this.appUser.username}, id: ${this.appUser.appUserId})`
    }

    link() {
        return <Link to={`/submissions/${this.submissionId}`}>Inspect</Link>
    }
}
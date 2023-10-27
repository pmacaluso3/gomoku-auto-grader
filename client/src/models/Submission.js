import { Link } from "react-router-dom"

import Record from "./Record"
import formatDateTime from "../utils/formatDateTime"
import Applicant from "./Applicant"

export default class Submission extends Record {
    constructor({ submissionId, appUserId, gradingBatchId, createdAt, gradedAt, testCaseOutcomes, appUser, archived }) {
        super()
        this.submissionId = submissionId
        this.appUserId = appUserId
        this.appUser = new Applicant(appUser || {})  
        this.gradingBatchId = gradingBatchId
        this.createdAt = createdAt
        this.gradedAt = gradedAt
        this.testCaseOutcomes = testCaseOutcomes
        this.archived = archived
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
        return this.appUser && this.appUser.userInfo()
    }

    timeGraded() {
        return formatDateTime(this.gradedAt)
    }

    timeCreated() {
        return formatDateTime(this.createdAt)
    }

    isArchived() {
        return this.archived ? "✅" : "❌"        
    }

    link() {
        return <Link to={`/submissions/${this.submissionId}`}>Inspect</Link>
    }
}
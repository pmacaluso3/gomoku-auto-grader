import { useContext, useEffect, useState } from "react"
import { useParams } from "react-router-dom"
import UserContext from "../contexts/UserContext"
import SubmissionModel from "../models/Submission"
import Applicant from "../models/Applicant"
import Table from "../components/Table"
import Input from "../components/Input"
import TestCaseOutcome from "../models/TestCaseOutcome"

const Submission = () => {
    const DEFAULT_FILTER_STATE = {
        passing: false,
        failing: false,
        hasBoard: false
    }
    const [filterState, setFilterState] = useState(DEFAULT_FILTER_STATE)
    const [submission, setSubmission] = useState({})
    const [filteredTestCases, setFilteredTestCases] = useState(submission.testCaseOutcomes || [])
    const [applicant, setApplicant] = useState({})

    const { submissionId } = useParams()

    const userObj = useContext(UserContext)

    const fetchSubmission = () => {
        if (!submissionId) return
        userObj.authGet(`/submissions/${submissionId}`)
        .then(response => response.json())
        .then(s => setSubmission(new SubmissionModel(s)))
    }
    useEffect(fetchSubmission, [submissionId])

    const setApplicantFromSubmission = () => {
        if (submission && submission.appUser) {
            setApplicant(new Applicant(submission.appUser))
        }
    }
    useEffect(setApplicantFromSubmission, [submission])

    const filterTestCases = () => {
        let newlyFilteredTestCases
        if (submission.testCaseOutcomes) {
            newlyFilteredTestCases = submission.testCaseOutcomes.map(tco => new TestCaseOutcome(tco))
        } else {
            newlyFilteredTestCases = []
        }
        
        if (filterState.passing) {
            newlyFilteredTestCases = newlyFilteredTestCases.filter(t => t.success)
        }

        if (filterState.failing) {
            newlyFilteredTestCases = newlyFilteredTestCases.filter(t => !t.success)
        }

        if (filterState.hasBoard) {
            newlyFilteredTestCases = newlyFilteredTestCases.filter(t => t.boardState)
        }

        setFilteredTestCases(newlyFilteredTestCases)
    }
    useEffect(filterTestCases, [submission, filterState])

    return (
        <>
            <h3>Submission {submissionId}</h3>
            <div>By applicant #{applicant.appUserId}, {applicant.firstName} {applicant.lastName} (username {applicant.username})</div>
            <Input name="passing" type="checkbox" formState={filterState} setter={setFilterState} />
            <Input name="failing" type="checkbox" formState={filterState} setter={setFilterState} />
            <Input name="hasBoard" type="checkbox" formState={filterState} setter={setFilterState} />
            <Table records={filteredTestCases} keys={["testName", "passed", "description", "boardState",, "manuallyEdited", "markAsSuccessful"]}/>
        </>
    )
}

export default Submission
import { useContext, useEffect, useState } from "react"
import { Link, useParams } from "react-router-dom"
import UserContext from "../contexts/UserContext"
import Table from "../components/Table"
import TestCaseOutcomeModel from "../models/TestCaseOutcome"

const TestCaseOutcome = () => {    
    const [testCaseOutcome, setTestCaseOutcome] = useState(new TestCaseOutcomeModel({}))

    const { testCaseOutcomeId } = useParams()

    const userObj = useContext(UserContext)

    const fetchTestCaseOutcome = () => {
        userObj.authGet(`/test_case_outcomes/${testCaseOutcomeId}`)
        .then(response => response.json())
        .then(tco => setTestCaseOutcome(new TestCaseOutcomeModel(tco)))
    }
    useEffect(fetchTestCaseOutcome, [testCaseOutcomeId])

    const updateTestCaseOutcome = (success) => {
        userObj.authPut("/test_case_outcomes", { testCaseOutcomeId, success })
        .then(response => response.json())
        .then(tco => setTestCaseOutcome(new TestCaseOutcomeModel(tco)))
    }

    return (
        <>
            <Table records={[testCaseOutcome]} keys={["testName", "passed", "description", "boardState", "manuallyEdited"]}/>
            <button onClick={() => updateTestCaseOutcome(true)}>Mark as passing</button>
            <button onClick={() => updateTestCaseOutcome(false)}>Mark as failing</button>
            <Link to={`/submissions/${testCaseOutcome.getValue("submissionId")}`}>Back to submission</Link>
        </>
    )
}

export default TestCaseOutcome
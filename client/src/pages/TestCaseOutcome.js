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
            <Table records={[testCaseOutcome]} keys={["testName", "passed", "description", "board", "manuallyEdited"]}/>
            <div className="spaced">
                <button className="btn btn-success"onClick={() => updateTestCaseOutcome(true)}>Mark as passing</button>
                <button className="btn btn-danger" onClick={() => updateTestCaseOutcome(false)}>Mark as failing</button>
                <Link to={`/submissions/${testCaseOutcome.getValue("submissionId")}`}>Back to submission</Link>
            </div>
        </>
    )
}

export default TestCaseOutcome
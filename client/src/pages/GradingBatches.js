import { useContext, useEffect, useState } from "react"
import UserContext from "../contexts/UserContext"
import Table from "../components/Table"

const GradingBatches = () => {
    const [gradingBatches, setGradingBatches] = useState([])

    const userObj = useContext(UserContext)

    const fetchGradingBatches = () => {
        userObj.authGet("/grading_batches")
        .then(response => response.json())
        .then(body => setGradingBatches(body))
    }
    useEffect(fetchGradingBatches, [])

    return (
        <>
        <h3>Grading Batches</h3>
        <Table records={gradingBatches} keys={["createdAt", "gradingBatchId"]} />
        </>
    )
}

export default GradingBatches
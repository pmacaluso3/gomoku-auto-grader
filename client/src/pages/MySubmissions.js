import { useContext, useEffect, useState } from "react"
import UserContext from "../contexts/UserContext"
import Table from "../components/Table"
import Submission from "../models/Submission"

const MySubmissions = () => {
    const [submissions, setSubmissions] = useState([])

    const userObj = useContext(UserContext)

    const fetchSubmissions = () => {
        userObj.authGet("/submissions/mine")
        .then(response => response.json())
        .then(data => setSubmissions(data.map(s => new Submission(s))))
    }
    useEffect(fetchSubmissions, [])

    return (
        <>
            <h3>My Submissions</h3>
            <Table records={submissions} keys={["timeCreated", "timeGraded", "numberOfPassingTests", "numberOfFailingTests"]} />
        </>
    )
}

export default MySubmissions
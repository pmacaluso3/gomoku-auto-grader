import { useContext, useEffect, useState } from "react"
import UserContext from "../contexts/UserContext"
import SubmissionsList from "../components/submissions/SubmissionsList"

const MySubmissions = () => {
    const [submissions, setSubmissions] = useState([])

    const userObj = useContext(UserContext)

    const fetchSubmissions = () => {
        userObj.authGet("/submissions/mine")
        .then(response => response.json())
        .then(data => setSubmissions(data))
    }
    useEffect(fetchSubmissions, [])

    return (
        <>
            <h3>My Submissions</h3>
            <SubmissionsList submissions={submissions} />
        </>
    )
}

export default MySubmissions
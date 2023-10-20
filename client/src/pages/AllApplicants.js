import { useContext, useEffect, useState } from "react"
import UserContext from "../contexts/UserContext"
import Table from "../components/Table"

const AllApplicants = () => {
    const [allApplicants, setAllApplicants] = useState([])

    const userObj = useContext(UserContext)

    const fetchAllApplicants = () => {
        userObj.authGet("/users/applicants")
        .then(response => response.json())
        .then(body => setAllApplicants(body))
    }
    useEffect(fetchAllApplicants, [])

    return (
        <>
            <h3>All Applicants</h3>
            <Table records={allApplicants} keys={["appUserId", "username", "accountSetupToken", "firstName", "lastName"]} />
        </>
    )
}

export default AllApplicants
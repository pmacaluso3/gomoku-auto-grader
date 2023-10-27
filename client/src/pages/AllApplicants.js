import { useContext, useEffect, useState } from "react"
import UserContext from "../contexts/UserContext"
import Table from "../components/Table"
import Applicant from "../models/Applicant"
import Input from "../components/Input"
import Errors from "../components/Errors"

const AllApplicants = () => {
    const DEFAULT_FILTERS_STATE = { userInfo: "", requiresSetup: false }
    const [filterState, setFilterState] = useState(DEFAULT_FILTERS_STATE)
    const [allApplicants, setAllApplicants] = useState([])
    const [filteredApplicants, setFilteredApplicants] = useState([])
    const [errors, setErrors] = useState([])
    const [successMessage, setSuccessMessage] = useState("")

    const userObj = useContext(UserContext)

    const fetchAllApplicants = () => {
        userObj.authGet("/users/applicants")
        .then(response => response.json())
        .then(applicants => setAllApplicants(applicants.map(a => new Applicant(a))))
    }
    useEffect(fetchAllApplicants, [])

    const filterApplicants = () => {
        let newlyFilteredApplicants = [...allApplicants]

        if (filterState.userInfo) {
            newlyFilteredApplicants = newlyFilteredApplicants.filter(a => a.getValue("userInfo").includes(filterState.userInfo))
        }

        if (filterState.requiresSetup) {
            newlyFilteredApplicants = newlyFilteredApplicants.filter(a => !a.getValue("hasBeenSetup"))
        }

        setFilteredApplicants(newlyFilteredApplicants)
    }
    useEffect(filterApplicants, [allApplicants, filterState])

    const sendSetupEmails = () => {
        const ids = filteredApplicants.map(a => a.getValue("appUserId")).join(",")
        userObj.authPost(`/users/send_setup_emails/${ids}`, {})
        .then(response => {
            if (response.ok) {
                setErrors([])
                setSuccessMessage(`Emails sent!`)
            } else {
                response.json().then(errors => {
                    setErrors(errors)
                    setSuccessMessage("")
                })
            }
        })
    }

    return (
        <>
            <h3>All Applicants</h3>
            <div className="filter-controls spaced">
                <Input type="text" name="userInfo" formState={filterState} setter={setFilterState} />
                <Input type="checkbox" name="requiresSetup" formState={filterState} setter={setFilterState} />
            </div>
            <Errors errors={errors} />
            <ul className="text-success">{successMessage}</ul>
            <Table records={filteredApplicants} keys={["appUserId", "username", "accountSetupToken", "firstName", "lastName", "setupComplete"]} />
            <button onClick={sendSetupEmails} className="btn btn-primary">Send setup email to selected applicants</button>
        </>
    )
}

export default AllApplicants
import { useContext, useEffect, useState } from "react"

import UserContext from "../contexts/UserContext"
import Table from "../components/Table"
import Input from "../components/Input"
import Errors from "../components/Errors"
import Submission from "../models/Submission"

const AllSubmissions = () => {
    const DEFAULT_FILTERS_STATE = {
        graded: false,
        ungraded: false,
        gradingBatchId: "",
        passing: false,
        failing: false,
        userInfo: "",
        includeArchived: false
    }
    const [allSubmissions, setAllSubmissions] = useState([])
    const [filteredSubmissions, setFilteredSubmissions] = useState([])
    const [filterState, setFilterState] = useState(DEFAULT_FILTERS_STATE)
    const [gradingBatchIds, setGradingBatchIds] = useState([])
    const [errors, setErrors] = useState([])
    const [successMessages, setSuccessMessages] = useState([])
    
    const userObj = useContext(UserContext)

    const fetchSubmissions = () => {
        userObj.authGet("/submissions/all")
        .then(response => response.json())
        .then(submissions => setAllSubmissions(submissions.map(s => new Submission(s))))
    }
    useEffect(fetchSubmissions, [])

    const getGradingBatchIdsFromSubmissions = () => {
        const ids = filteredSubmissions.map(s => s.getValue("gradingBatchId"))
        const idsWithBlank = ["", ...ids]
        const uniqueIds = Array.from(new Set(idsWithBlank)).filter(i => i !== 0)
        setGradingBatchIds(uniqueIds)
    }
    useEffect(getGradingBatchIdsFromSubmissions, [filteredSubmissions])

    const applyFilters = () => {
        let newlyFilteredSubmissions = [...allSubmissions]
        if (filterState.graded) {
            newlyFilteredSubmissions = newlyFilteredSubmissions.filter(s => s.getValue("gradedAt") !== null)
        }
        if (filterState.ungraded) {
            newlyFilteredSubmissions = newlyFilteredSubmissions.filter(s => s.getValue("gradedAt") === null)
        }
        if (filterState.passing) {
            newlyFilteredSubmissions = newlyFilteredSubmissions.filter(s => s.getValue("numberOfFailingTests") === 0 && s.getValue("gradedAt") !== null)
        }
        if (filterState.failing) {
            newlyFilteredSubmissions = newlyFilteredSubmissions.filter(s => s.getValue("numberOfFailingTests") > 0 && s.getValue("gradedAt") !== null)
        }
        if (filterState.gradingBatchId) {
            newlyFilteredSubmissions = newlyFilteredSubmissions.filter(s => s.getValue("gradingBatch") === parseInt(filterState.gradingBatchId))
        }
        if (filterState.userInfo) {
            newlyFilteredSubmissions = newlyFilteredSubmissions.filter(s => s.getValue("userInfo").includes(filterState.userInfo))
        }

        if (!filterState.includeArchived) {
            newlyFilteredSubmissions = newlyFilteredSubmissions.filter(s => !s.getValue("archived"))
        }

        setFilteredSubmissions(newlyFilteredSubmissions)
    }
    useEffect(applyFilters, [allSubmissions, filterState])

    const downloadSelectedZips = () => {
        const ids = filteredSubmissions.map(s => s.submissionId).join(",")
        userObj.authGet(`/submissions/zip_files/${ids}`)
        .then(response => response.blob())
        .then(blob => {
            const url = window.URL.createObjectURL(blob)
            const a = document.createElement('a')
            a.href = url
            a.download = `applicant-zips-${(new Date()).getTime()}.zip`
            document.body.appendChild(a)
            a.click()    
            a.remove()
        })
    }

    const archiveSelectedSubmissions = () => {
        const ids = filteredSubmissions.map(s => s.submissionId).join(",")
        
        if (!window.confirm(`Are you sure you want to archive: ${ids}?`)) return

        userObj.authPut(`/submissions/bulk_archive/${ids}`)
        .then(response => {
            if (response.ok) {
                response.json().then(results => {
                    setErrors(results.failures)
                    setSuccessMessages(results.successes)
                    fetchSubmissions()
                    setFilterState(DEFAULT_FILTERS_STATE)
                })
            } else {
                response.json().then(errors => {
                    setErrors(errors)
                    setSuccessMessages([])
                })
            }
        })
    }

    return (
        <>
            <h3>All Submissions</h3>
            <div className="filter-controls spaced">
                <Input type="select" name="gradingBatchId" formState={filterState} setter={setFilterState} options={gradingBatchIds} />
                <Input name="userInfo" type="text" formState={filterState} setter={setFilterState} />
                <Input type="checkbox" name="graded" formState={filterState} setter={setFilterState} />
                <Input type="checkbox" name="ungraded" formState={filterState} setter={setFilterState} />
                <Input type="checkbox" name="passing" formState={filterState} setter={setFilterState} />
                <Input type="checkbox" name="failing" formState={filterState} setter={setFilterState} />
                <Input type="checkbox" name="includeArchived" formState={filterState} setter={setFilterState} />
            </div>
            <Errors errors={errors} />
            <Errors errors={successMessages} className="text-success" />
            <Table records={filteredSubmissions} keys={ ["submissionId", "timeGraded", "timeCreated", "gradingBatch", "numberOfFailingTests", "numberOfPassingTests", "link", "userInfo", "isArchived"] } />
            <div className="spaced">
                <button className="btn btn-primary" onClick={downloadSelectedZips}>Download selected zips</button>
                <button className="btn btn-danger" onClick={archiveSelectedSubmissions}>Archive selected submissions</button>
            </div>
        </>
    )
}

export default AllSubmissions
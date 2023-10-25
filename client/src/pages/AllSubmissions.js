import { useContext, useEffect, useState } from "react"

import UserContext from "../contexts/UserContext"
import Table from "../components/Table"
import Input from "../components/Input"
import Submission from "../models/Submission"

const AllSubmissions = () => {
    const DEFAULT_FILTERS_STATE = {
        graded: false,
        ungraded: false,
        gradingBatchId: "",
        passing: false,
        failing: false
    }
    const [allSubmissions, setAllSubmissions] = useState([])
    const [filteredSubmissions, setFilteredSubmissions] = useState([])
    const [filterState, setFilterState] = useState(DEFAULT_FILTERS_STATE)
    const [gradingBatchIds, setGradingBatchIds] = useState([])
    
    const userObj = useContext(UserContext)

    const fetchSubmissions = () => {
        userObj.authGet("/submissions/all")
        .then(response => response.json())
        .then(submissions => setAllSubmissions(submissions.map(s => new Submission(s))))
    }
    useEffect(fetchSubmissions, [])

    const getGradingBatchIdsFromSubmissions = () => {
        const ids = allSubmissions.map(s => s.getValue("gradingBatchId"))
        const idsWithBlank = ["", ...ids]
        const uniqueIds = Array.from(new Set(idsWithBlank)).filter(i => i !== 0)
        setGradingBatchIds(uniqueIds)
    }
    useEffect(getGradingBatchIdsFromSubmissions, [allSubmissions])

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
            newlyFilteredSubmissions = newlyFilteredSubmissions.filter(s => s.getValue("gradingBatch") == filterState.gradingBatchId)
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

    return (
        <>
            <h3>All Submissions</h3>
            {/* TODO: put these inputs into a horizontal rack */}
            <Input type="select" name="gradingBatchId" formState={filterState} setter={setFilterState} options={gradingBatchIds} />
            <Input type="checkbox" name="graded" formState={filterState} setter={setFilterState} />
            <Input type="checkbox" name="ungraded" formState={filterState} setter={setFilterState} />
            <Input type="checkbox" name="passing" formState={filterState} setter={setFilterState} />
            <Input type="checkbox" name="failing" formState={filterState} setter={setFilterState} />
            <Table records={filteredSubmissions} keys={ ["gradedAt", "createdAt", "gradingBatch", "numberOfFailingTests", "numberOfPassingTests", "link"] } />
            <button onClick={downloadSelectedZips}>Download selected zips</button>
        </>
    )
}

export default AllSubmissions
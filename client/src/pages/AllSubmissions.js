import { useContext, useEffect, useState } from "react"

import UserContext from "../contexts/UserContext"
import Table from "../components/Table"

const AllSubmissions = () => {
    const [allSubmissions, setAllSubmissions] = useState([])
    const [filteredSubmissions, setFilteredSubmissions] = useState([])
    
    const userObj = useContext(UserContext)

    const fetchSubmissions = () => {
        userObj.authGet("/submissions/all")
        .then(response => response.json())
        .then(body => setAllSubmissions(body))
    }
    useEffect(fetchSubmissions, [])

    const applyFilters = () => {
        const newlyFilteredSubmissions = [...allSubmissions]
        // apply filters here
        setFilteredSubmissions(newlyFilteredSubmissions)
    }
    useEffect(applyFilters, [allSubmissions])

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
            <Table records={filteredSubmissions} keys={ ["gradedAt", "createdAt"] } />
            <button onClick={downloadSelectedZips}>Download selected zips</button>
        </>
    )
}

export default AllSubmissions
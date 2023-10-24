require("dotenv").config()
const fs = require("fs")
const { exec } = require("child_process")
const { stderr } = require("process")
const backendUrl = process.env.BACKEND_URL
const username = process.env.ADMIN_USERNAME
const password = process.env.ADMIN_PASSWORD
const applicantZipsFolder = `${__dirname}/applicant-zips`
const unzippedSubmissionsFolder = `${__dirname}/unzipped-submissions`
const testSuiteFolder = `${__dirname}/gomoku-test-suite`
const unzipDestination = "src/main/java/learn/gomoku"

const run = (cmd) => {
    exec(`powershell -Command "${cmd}"`, (err, stdErr) => {
        if (err) {
            console.log("err: ", err)
        }
        if (stderr && stdErr.length > 0) {
            console.log("stdErr: ", stdErr)
        }
    })
}


const authenticate = async () => {
    const response = await fetch(backendUrl + "/users/authenticate", {
        method: "POST",
        headers: {
            "Content-Type": "application/json",
            Accept: "application/json"
        },
        body: JSON.stringify({ username, password })
    })
    const { jwt_token } = await response.json()
    return jwt_token
}

const createGradingBatch = async (token) => {
    const response = await fetch(backendUrl + "/grading_batches", {
        method: "POST",
        headers: {
            "Content-Type": "application/json",
            Accept: "application/json",
            Authorization: `Bearer ${token}`
        }
    })
    const { gradingBatchId } = await response.json()
    return gradingBatchId
}

const markGraded = async ({ submissionId, gradingBatchId, token }) => {
    const response = await fetch(backendUrl + "/submissions/mark_graded", {
        method: "PUT",
        headers: {
            "Content-Type": "application/json",
            Accept: "application/json",
            Authorization: `Bearer ${token}`
        },
        body: JSON.stringify({ submissionId, gradingBatchId })
    })
}

const handleZipFile = ({ file, token, gradingBatchId }) => {
    if (!file.endsWith(".zip")) { return }
    
    const submissionId = file.replace(/.zip$/, "")
    markGraded(submissionId, gradingBatchId, token)
    const copyTo = `${unzippedSubmissionsFolder}/${file.replace(".zip", "")}`
    const unzipTo = `${copyTo}/${unzipDestination}`
    run(`Copy-Item -R ${testSuiteFolder} ${copyTo}`)
    run(`Expand-Archive -Path ${applicantZipsFolder}/${file} -DestinationPath ${unzipTo}`)
    
    run(`export ADMIN_TOKEN=${token}`)
    run(`export API_URL=${backendUrl}`)
    run(`export SUBMISSION_ID=${submissionId}`)

    run(`mvn -f ${copyTo} clean install`)

    run(`unset ADMIN_TOKEN`)
    run(`unset API_URL`)
    run(`unset SUBMISSION_ID`)
}

fs.readdir(applicantZipsFolder, async (error, files) => {
    if (error) {
        console.log(error)
    } else {
        const token = await authenticate()
        const gradingBatchId = await createGradingBatch(token)
        for (const file of files) {
            handleZipFile({ file, token, gradingBatchId })
        }
    }
})
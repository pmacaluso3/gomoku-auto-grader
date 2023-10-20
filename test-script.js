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

const markGraded = async (submissionId, gradingBatchId, token) => {
    const response = await fetch(backendUrl + "/submissions/mark_graded", {
        method: "PUT",
        headers: {
            "Content-Type": "application/json",
            Accept: "application/json",
            Authorization: `Bearer ${token}`
        },
        body: JSON.stringify({ submissionId, gradingBatchId })
    })
    return await response.json()
}

const handleZipFile = ({ file, token, gradingBatchId }) => {
    if (!file.endsWith(".zip")) { return }
    
    const submissionId = "TODO" // once we have zipFile exports, derive the submissionId from the file's name
    markGraded(submissionId, gradingBatchId, token)
    const copyTo = `${unzippedSubmissionsFolder}/${file.replace(".zip", "")}`
    const unzipTo = `${copyTo}/${unzipDestination}`
    run(`Copy-Item -R ${testSuiteFolder} ${copyTo}`)
    run(`Expand-Archive -Path ${applicantZipsFolder}/${file} -DestinationPath ${unzipTo}`)
    // run(`cd ${copyTo}`)
    // run("java -jar junit-platform-console-standalone-1.10.0.jar execute")
    // java -jar junit-platform-console-standalone-1.10.0.jar execute <OPTIONS>
    // in this current submission folder that we're iterating through:
    // set env vars: token, backend url, submission id, gradingBatch id
    // reload maven dependencies
    // run all junit tests
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

fs.readdir(applicantZipsFolder, async (error, files) => {
    if (error) {
        console.log(error)
    } else {
        const token = await authenticate()
        const gradingBatchId = createGradingBatch(token)
        // const token = "asdf"
        // const gradingBatchId = 1
        for (const file of files) {
            handleZipFile({ file, token, gradingBatchId })
        }
    }
})
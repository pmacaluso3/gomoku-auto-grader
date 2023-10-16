const fs = require("fs")
const { exec } = require("child_process")
const { stderr } = require("process")
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

fs.readdir(applicantZipsFolder, (error, files) => {
    if (error) {
        console.log(error)
    } else {
        // generate a gradingBatch id?
        for (const file of files) {
            if (file.endsWith(".zip")) {
                const copyTo = `${unzippedSubmissionsFolder}/${file.replace(".zip", "")}`
                const unzipTo = `${copyTo}/${unzipDestination}`
                run(`Copy-Item -R ${testSuiteFolder} ${copyTo}`)
                run(`Expand-Archive -Path ${file} -DestinationPath ${unzipTo}`)
                // in this current submission folder that we're iterating through:
                // set env vars: backend url, applicant id, submission id, gradingBatch id?, basic auth creds for report backend? 
                // reload maven dependencies
                // run all junit tests
            }
        }
    }
})
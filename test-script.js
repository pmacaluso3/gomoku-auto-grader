const fs = require("fs")
const { exec } = require("child_process")
const testSuiteFolder = "gomoku-test-suite"
const unzipDestination = "src/main/java/learn/gomoku"

const run = (cmd) => {
    exec(`powershell -Command "${cmd}"`, (err, stdErr) => {
        console.log("err: ", err)
        console.log("stdErr: ", stdErr)
    })
}

fs.readdir(__dirname, (error, files) => {
    if (error) {
        console.log(error)
    } else {
        // generate a gradingBatch id?
        for (const file of files) {
            if (file.endsWith(".zip")) {
                const copyTo = `${file}-test`
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
const fs = require("fs")
const { exec } = require("child_process")
const { stderr } = require("process")
const applicantZipsFolder = `${__dirname}/applicant-zips`
const unzippedSubmissionsFolder = `${__dirname}/unzipped-submissions`

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

run(`rm -R -Force ${unzippedSubmissionsFolder}/*`)
// run(`rm -R -Force ${applicantZipsFolder}/*`)
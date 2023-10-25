require("dotenv").config()
const fs = require("fs")
const { exec } = require("child_process")
const backendUrl = process.env.BACKEND_URL
const username = process.env.ADMIN_USERNAME
const password = process.env.ADMIN_PASSWORD
const applicantZipsFolder = `${__dirname}/applicant-zips`
const unzippedSubmissionsFolder = `${__dirname}/unzipped-submissions`
const testSuiteFolder = `${__dirname}/gomoku-test-suite`
const unzipDestination = "src/main/java/learn/gomoku"

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

const markGraded = ({ submissionId, gradingBatchId, token }) => {
    fetch(backendUrl + "/submissions/mark_graded", {
        method: "PUT",
        headers: {
            "Content-Type": "application/json",
            Accept: "application/json",
            Authorization: `Bearer ${token}`
        },
        body: JSON.stringify({ submissionId, gradingBatchId })
    })
}

const setEnvVars = async (projectFolder, vars) => {
    const pomPath = projectFolder + "/pom.xml"
    const pom = fs.readFileSync(pomPath).toString()
    const boundary = "</properties>"
    let [firstHalf, secondHalf] = pom.split(boundary)
    firstHalf += boundary

    let varsString = ""
    for (const key of Object.keys(vars)) {
        varsString += `<${key}>${vars[key]}</${key}>`
    }

    const varsInPom = 
    `<build>
        <plugins>
            <plugin>
                <groupId>org.apache.maven.plugins</groupId>
                <artifactId>maven-surefire-plugin</artifactId>
                <configuration>
                    <systemPropertyVariables>`
                    +
                        varsString
                    +
                    `</systemPropertyVariables>
                </configuration>
            </plugin>
        </plugins>
    </build>`

    fs.writeFileSync(pomPath, firstHalf + varsInPom + secondHalf)
}

const handleZipFile = ({ file, token, gradingBatchId }) => {
    if (!file.endsWith(".zip")) { return }
    
    const submissionId = file.replace(/.zip$/, "")
    markGraded({ submissionId, gradingBatchId, token })
    const copyTo = `${unzippedSubmissionsFolder}/${file.replace(".zip", "")}`
    const unzipTo = `${copyTo}/${unzipDestination}`    
    exec(`powershell -Command "Copy-Item -R ${testSuiteFolder} ${copyTo}"`, (_err, _stdErr) => {
        exec(`powershell -Command "Expand-Archive -Path ${applicantZipsFolder}/${file} -DestinationPath ${unzipTo}"`, (_err, _stdErr) => {
            setEnvVars(copyTo, { ADMIN_TOKEN: token, API_URL: backendUrl, SUBMISSION_ID: submissionId })
            exec(`powershell -Command "mvn -f ${copyTo} clean install"`)
        })        
    })
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
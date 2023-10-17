import SubmissionRow from "./SubmissionRow"
import SubmissionHeader from "./SubmissionHeader"

const SubmissionsList = ({ submissions }) => {
    return (
        <table>
            <SubmissionHeader />
            <tbody>
                { submissions.map(submission => <SubmissionRow
                    key={submission.submissionId}
                    submission={submission}
                />)}
            </tbody>
        </table>
    )
}

export default SubmissionsList
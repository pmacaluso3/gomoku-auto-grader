const SubmissionRow = ({ submission }) => {
    return (
        <tr>
            <td>{submission.submittedAt}</td>
            <td>{submission.gradedAt}</td>
            <td></td>
            <td></td>
        </tr>
    )
}

export default SubmissionRow
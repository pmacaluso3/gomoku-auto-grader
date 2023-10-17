const SubmissionRow = ({ submission }) => {
    return (
        <tr>
            <td>{submission.createdAt}</td>
            <td>{submission.gradedAt}</td>
            <td></td>
            <td></td>
        </tr>
    )
}

export default SubmissionRow
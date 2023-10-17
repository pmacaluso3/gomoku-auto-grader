const SubmissionHeader = ({ submission }) => {
    return (
        <thead>
            <tr>
                <th>Submitted at:</th>
                <th>Graded at:</th>
                <th>Passed test cases:</th>
                <th>Failed test cases:</th>
            </tr>
        </thead>
    )
}

export default SubmissionHeader
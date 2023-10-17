const Errors = ({ errors }) => {
    return (
        <ul className="text-danger">
            { errors.map(error => <li key={error}>{error}</li>)}
        </ul>
    )
}

export default Errors
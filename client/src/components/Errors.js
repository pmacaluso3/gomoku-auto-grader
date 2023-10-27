const Errors = ({ errors, className }) => {
    return (
        <ul className={ className || "text-danger" }>
            { errors.map(error => <li key={error}>{error}</li>)}
        </ul>
    )
}

export default Errors
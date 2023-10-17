import humanize from "../utils/humanize"

const Input = ({ name, type, formState, setter }) => {
    type = type || "text"
    if (name === "password") { type = "password" }

    const onChange = (event) => {
        const value = type === "file" ? event.target.files[0] : event.target.value

        setter({ ...formState, [event.target.name]: value })
    }

    const inputProps = {
        type,
        name,
        onChange,
        id: `${name}-input`,
    }
    if (type !== "file") {
        inputProps.value = formState[name]
    }

    return (
        <fieldset className="form-group">
            <label htmlFor={`${name}-input`}>{humanize(name)}:</label>
            <input { ...inputProps } />
        </fieldset>
)}

export default Input
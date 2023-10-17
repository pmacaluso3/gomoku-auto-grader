import humanize from "../utils/humanize"

const Input = ({ name, type, formState, setter }) => {
    type = type || "text"

    const onChange = (event) => {
        setter({ ...formState, [event.target.name]: event.target.value })
    }

    return (
        <fieldset className="form-group">
            <label htmlFor={`${name}-input`}>{humanize(name)}:</label>
            <input type={type} id={`${name}-input`} name={name} value={formState[name]} onChange={onChange} />
        </fieldset>
)}

export default Input
import humanize from "../utils/humanize"

const Input = ({ name, type, formState, setter, options }) => {
    type = type || "text"
    if (name === "password") { type = "password" }

    const onChange = (event) => {
        let value;
        if (type === "file") {
            value = event.target.files[0]
        } else if (type === "checkbox") {
            value = event.target.checked
        } else {
            value = event.target.value
        }

        setter({ ...formState, [event.target.name]: value })
    }

    const id = `${name}-input` 
    const inputProps = {
        type,
        name,
        onChange,
        id,
    }
    if (type === "checkbox") {
        inputProps.checked = formState[name]
    } else if (type !== "file") {
        inputProps.value = formState[name]
    }

    let tag
    if (type === "select") {
        tag = <select { ...inputProps }>
            { options.map(o => <option key={o} value={o}>{o}</option>)}
        </select>
    } else {
        tag = <input { ...inputProps } />
    }

    return (
        <fieldset className="form-group">
            <label htmlFor={id}>{humanize(name)}:</label>
            {tag}
        </fieldset>
    )
}

export default Input
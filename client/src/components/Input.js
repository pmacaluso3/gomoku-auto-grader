import humanize from "../utils/humanize"

const Input = ({ name, type, formState, setter, options, style }) => {
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
        style,
        className: type === "checkbox" ? "form-check-input" : "form-control"
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
    } else if (type === "textarea") {
        tag = <textarea { ...inputProps } />
    } else {
        tag = <input { ...inputProps } />
    }

    return (
        <div className={ type === "checkbox" ? "form-check" : "form-group"}>
            <label className={ type === "checkbox" ? "form-check-label": null } htmlFor={id}>{humanize(name)}:</label>
            <br/>
            {tag}
        </div>
    )
}

export default Input
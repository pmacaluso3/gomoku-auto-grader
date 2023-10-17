import { useContext, useState } from "react"
import UserContext from "../contexts/UserContext"
import Errors from "../components/Errors"
import Input from "../components/Input"

const CreateApplicant = () => {
    const DEFAULT_FORM = { username: "" }
    const [formState, setFormState] = useState(DEFAULT_FORM)
    const [successMessage, setSuccessMessage] = useState("")
    const [errors, setErrors] = useState([])

    const userObj = useContext(UserContext)
    
    const handleSubmit = (event) => {
        event.preventDefault()
        userObj.authPost("/users", formState)
        .then(response => {
            if (response.ok) {
                setErrors([])
                setSuccessMessage(`Applicant ${formState.username} created!`)
                setFormState(DEFAULT_FORM)
            } else {
                response.json().then(errors => {
                    setErrors(errors)
                    setSuccessMessage("")
                })
            }
        })
    }
    
    return (
        <>
            <h3>Create Applicants</h3>
            <ul className="text-success">{successMessage}</ul>
            <Errors errors={errors} />
            <form onSubmit={handleSubmit}>
                <Input name="username" formState={formState} setter={setFormState} />
            <button type="submit">Create!</button>
            </form>
        </>
    )
}

export default CreateApplicant
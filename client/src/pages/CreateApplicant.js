import { useContext, useState } from "react"
import UserContext from "../contexts/UserContext"
import Errors from "../components/Errors"
import Input from "../components/Input"

const CreateApplicant = () => {
    const DEFAULT_FORM = { usernames: "" }
    const [formState, setFormState] = useState(DEFAULT_FORM)
    const [successMessages, setSuccessMessages] = useState([])
    const [errors, setErrors] = useState([])

    const userObj = useContext(UserContext)
    
    const handleSubmit = (event) => {
        event.preventDefault()
        const usernames = formState.usernames.split(/\s/).filter(u => u.trim().length !== 0).join(",")
        userObj.authPost("/users", { usernames })
        .then(response => {
            if (response.ok) {
                response.json().then(results => {
                    setErrors(results.failures)
                    setSuccessMessages(results.successes)
                    setFormState(DEFAULT_FORM)  
                })
            } else {
                response.json().then(errors => {
                    setErrors(errors)
                    setSuccessMessages([])
                })
            }
        })
    }
    
    return (
        <>
            <h3>Create Applicants</h3>
            <p>Enter each email address on a separate line</p>
            <Errors errors={errors} />
            <Errors errors={successMessages} className="text-success" />
            <form onSubmit={handleSubmit}>
                <Input style={{ height: "30rem" }} type="textarea" name="usernames" formState={formState} setter={setFormState} />
            <button className="btn btn-primary" type="submit">Create!</button>
            </form>
        </>
    )
}

export default CreateApplicant
import { useContext, useState } from "react"
import UserContext from "../contexts/UserContext"
import Input from "../components/Input"
import { useNavigate } from "react-router-dom"
import Errors from "../components/Errors"

const CreateSubmission = () => {
    const DEFAULT_FORM = { zipFile: null }
    const [formState, setFormState] = useState(DEFAULT_FORM)
    const [errors, setErrors] = useState([])

    const navigate = useNavigate()

    const userObj = useContext(UserContext)

    const handleSubmit = (event) => {
        event.preventDefault()
        const formData = new FormData()
        formData.append("zipFile", formState.zipFile)
        userObj.authPost("/submissions", formData)
        .then(response => {
            if (response.ok) {
                navigate("/mySubmissions")
            } else {
                response.json()
                .then(errors => {
                    setErrors(errors)
                })
            }
        })
    }

    return (
        <>
            <h3>Create a Submission</h3>
            <Errors errors={errors} />
            <form id="submission-form" onSubmit={handleSubmit}>
                <Input name="zipFile" type="file" formState={formState} setter={setFormState} />
                <button type="submit">Submit!</button>
            </form>
        </>
    )
}

export default CreateSubmission
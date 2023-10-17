import { useContext, useState } from "react"
import { useNavigate, useParams } from "react-router-dom"
import UserContext from "../contexts/UserContext"
import Errors from "../components/Errors"
import Input from "../components/Input"
import { put } from "../utils/http"

const SetupAccount = () => {
    const DEFAULT_FORM = {
        firstName: "",
        lastName: "",
        password: "",
    }
    const [formState, setFormState] = useState(DEFAULT_FORM)
    const [errors, setErrors] = useState([])

    const { accountSetupToken } = useParams()

    const userObj = useContext(UserContext)

    const navigate = useNavigate()

    const handleSubmit = (event) => {
        event.preventDefault()
        put("/users/account_setup", { ...formState, accountSetupToken })
        .then(response => {
            if (response.ok) {
                navigate("/")
            } else {
                response.json()
                .then(errors => setErrors(errors))
            }
        })
    }

    return (
        <>
            <h3>Setup Your Account</h3>
            <Errors errors={errors} />
            <form onSubmit={handleSubmit}>
                {Object.keys(DEFAULT_FORM)
                .map(key => <Input
                    key={key}
                    name={key}
                    formState={formState}
                    setter={setFormState}
                />)}
                <button type="submit">Setup!</button>
            </form>
        </>
    )
}

export default SetupAccount
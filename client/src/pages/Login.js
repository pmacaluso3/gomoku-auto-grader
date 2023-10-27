import { useContext, useState } from "react"
import Input from "../components/Input"
import Errors from "../components/Errors"
import { post } from "../utils/http"
import UserContext from "../contexts/UserContext"

const Login = () => {
    const DEFAULT_FORM = {
        username: "",
        password: ""
    }
    const [formState, setFormState] = useState(DEFAULT_FORM)
    const [errors, setErrors] = useState([])

    const { login } = useContext(UserContext)
    
    const handleLogin = (event) => {
        event.preventDefault()
        post("/users/authenticate", formState)
        .then(response => {
            if (response.ok) {
                response.json().then(body => login(body.jwt_token))
            } else {
                setErrors(["Bad Credentials"])
            }
        })
    }

    return (
        <>
            <h2>Log In</h2>
            <Errors errors={errors} />
            <form onSubmit={handleLogin}>
                {Object.keys(DEFAULT_FORM)
                .map(key => <Input
                    key={key}
                    name={key}
                    formState={formState}
                    setter={setFormState}
                />)}
                <button className="btn btn-primary" type="submit">Log In</button>
            </form>
        </>
    )
}

export default Login
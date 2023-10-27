import { useContext } from "react"
import { Link } from "react-router-dom"

import abstractConditionalUserElement from "../utils/abstractConditionalUserElement"
import UserContext from "../contexts/UserContext"

const AuthLink = ({ to, text, loggedIn, requiredRole, onClick }) => {
    const userObj = useContext(UserContext)
    const user = userObj && userObj.user

    return abstractConditionalUserElement({
        elementIfYes: <li className="nav-item"><Link className="nav-link" to={to}>{text}</Link></li>,
        elementIfNo: null,
        loggedIn,
        user,
        requiredRole,
    })
}

const LoggedInLink = ({ to, text, requiredRole }) => {
    return AuthLink({ to, text, requiredRole, loggedIn: true })
}

const LoggedOutLink = ({ to, text}) => {
    return AuthLink({ to, text, loggedIn: false })
}

export { AuthLink, LoggedInLink, LoggedOutLink }
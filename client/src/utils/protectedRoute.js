import { Navigate } from "react-router-dom"
import abstractConditionalUserElement from "./abstractConditionalUserElement"

const NAVIGATE_TO = "/"

const protectedRoute = ({ element, loggedIn, user, requiredRole }) => {
    return abstractConditionalUserElement({
        elementIfYes: element,
        elementIfNo: <Navigate to={NAVIGATE_TO} />,
        loggedIn,
        user,
        requiredRole
    })
}

const loggedIn = (element, user, requiredRole) => {
    return protectedRoute({ element, user, requiredRole, loggedIn: true })
}

const loggedOut = (element, user) => {
    return protectedRoute({ element, user, loggedIn: false })
}

export { protectedRoute, loggedIn, loggedOut }
const abstractConditionalUserElement = ({
    elementIfYes,
    elementIfNo,
    loggedIn,
    user,
    requiredRole
}) => {
    const loggedInAndSupposedToBeLoggedIn = (user && loggedIn)
    const loggedOutAndSupposedToBeLoggedOut = (!user && !loggedIn)
    const hasRequiredRole = !requiredRole || (user && user.authorities && user.authorities.split(",").includes(requiredRole))
    if (
        (loggedInAndSupposedToBeLoggedIn && hasRequiredRole) ||
        loggedOutAndSupposedToBeLoggedOut
    ) {
        return elementIfYes
    } else {
        return elementIfNo
    }
}

export default abstractConditionalUserElement
import { Link } from "react-router-dom"
import { useContext } from "react"

import { LoggedInLink, LoggedOutLink } from "./AuthLink"
import UserContext from "../contexts/UserContext"
import spaceOut from "../utils/spaceOut"

const Nav = () => {
    const userObj = useContext(UserContext)

    return (
        <>
            { userObj && userObj.user &&
                <div>Hello, { userObj.user.sub}!</div>
            }
            
            <nav>
                { spaceOut(
                    // unconditional
                    <Link to="/">Home</Link>,

                    // logged out only
                    <LoggedOutLink to="/login" text="Login" />,
                    
                    // logged in only
                    <span onClick={userObj.logout}>
                        <LoggedInLink to="/" text="Logout" />
                    </span>,

                    // logged in admin only
                    <LoggedInLink to="/createApplicant" text="Create Applicant" requiredRole="ADMIN" />,

                    // logged in applicant only
                    <LoggedInLink to="/createSubmission" text="Create Submission" requiredRole="APPLICANT" />,
                    <LoggedInLink to="/mySubmissions" text="My Submissions" requiredRole="APPLICANT" />,
                )}
            </nav>
        </>
    )
}

export default Nav
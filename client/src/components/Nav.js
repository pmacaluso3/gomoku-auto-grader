import { Link } from "react-router-dom"
import { useContext } from "react"

import { LoggedInLink, LoggedOutLink } from "./AuthLink"
import UserContext from "../contexts/UserContext"

const Nav = () => {
    const userObj = useContext(UserContext)

    return (
        <nav className="navbar navbar-expand-lg navbar-light bg-light">
            { userObj && userObj.user &&
                <span>Hello, { userObj.user.sub}!</span>
            }
            
            <div className="collapse navbar-collapse">

                <ul className="nav navbar-nav">
                    {/* // unconditional */}
                    <li className="nav-item"><Link className="nav-link" to="/">Home</Link></li>

                    {/* // logged out only */}
                    <LoggedOutLink to="/login" text="Login" />
                    
                    {/* // logged in only */}
                    <span onClick={userObj.logout} style={{ alignSelf: "center" }}>
                        <LoggedInLink to="/" text="Logout" />
                    </span>

                    {/* // logged in admin only */}
                    <LoggedInLink to="/createApplicant" text="Create Applicant" requiredRole="ADMIN" />
                    <LoggedInLink to="/allApplicants" text="All Applicants" requiredRole="ADMIN" />
                    <LoggedInLink to="/gradingBatches" text="Grading Batches" requiredRole="ADMIN" />
                    <LoggedInLink to="/allSubmissions" text="All Submissions" requiredRole="ADMIN" />

                    {/* // logged in applicant only */}
                    <LoggedInLink to="/createSubmission" text="Create Submission" requiredRole="APPLICANT" />
                    <LoggedInLink to="/mySubmissions" text="My Submissions" requiredRole="APPLICANT" />
                </ul>
            </div>
        </nav>
    )
}

export default Nav
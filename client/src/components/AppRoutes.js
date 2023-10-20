import { useContext } from "react"
import { Routes, Route } from "react-router-dom"

import '../App.css';
import Login from "../pages/Login";
import { loggedIn, loggedOut } from "../utils/protectedRoute"
import UserContext from "../contexts/UserContext"
import Home from "../pages/Home";
import CreateApplicant from "../pages/CreateApplicant";
import SetupAccount from "../pages/SetupAccount";
import CreateSubmission from "../pages/CreateSubmission"
import MySubmissions from "../pages/MySubmissions"
import AllSubmissions from "../pages/AllSubmissions";
import GradingBatches from "../pages/GradingBatches";
import AllApplicants from "../pages/AllApplicants";

const AppRoutes = () => {
    const { user } = useContext(UserContext)

    return (
        <Routes>
          {/* unconditional */}
          <Route path="/" element={<Home />} />

          {/* loged out only */}
          <Route path="/login" element={loggedOut(<Login />, user)} />
          <Route path="/setupAccount/:accountSetupToken" element={<SetupAccount />}/>

          {/* loged in admin only */}
          <Route
            path="/createApplicant"
            element={loggedIn(<CreateApplicant />, user, "ADMIN")}
          />
          <Route
            path="/allApplicants"
            element={loggedIn(<AllApplicants />, user, "ADMIN")}
          />
          <Route
            path="/gradingBatches"
            element={loggedIn(<GradingBatches />, user, "ADMIN")}
          />
          <Route
            path="/allSubmissions"
            element={loggedIn(<AllSubmissions />, user, "ADMIN")}
          />

          {/* loged in applicant only */}
          <Route
            path="/createSubmission"
            element={loggedIn(<CreateSubmission />, user, "APPLICANT")}
          />
          <Route
            path="/mySubmissions"
            element={loggedIn(<MySubmissions />, user, "APPLICANT")}
          />
        </Routes>
    )
}   

export default AppRoutes
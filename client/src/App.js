import { BrowserRouter, Routes, Route } from "react-router-dom"
import { useEffect, useState } from "react";
import decode from "jwt-decode"

import './App.css';
import Login from "./pages/Login";
import Nav from "./components/Nav";
import { loggedIn, loggedOut } from "./utils/protectedRoute"
import { buildAuthRequests, post } from "./utils/http"
import UserContext from "./contexts/UserContext"
import Home from "./pages/Home";
import CreateApplicant from "./pages/CreateApplicant";
import SetupAccount from "./pages/SetupAccount";
import CreateSubmission from "./pages/CreateSubmission"
import MySubmissions from "./pages/MySubmissions"


function App() {
  const LOCAL_STORAGE_KEY = "user"
  
  const [user, setUser] = useState(null)
  const [hasAttemptedRestoreSession, setHasAttemptedRestoreSession] = useState(false)

  const login = (token) => {
    const fullUser = { ...decode(token), token }
    localStorage.setItem(LOCAL_STORAGE_KEY, JSON.stringify(fullUser))
    setUser(fullUser)
  }

  const logout = () => {
    localStorage.removeItem(LOCAL_STORAGE_KEY)
    setUser(null)
  }

  const authRequests = user && buildAuthRequests(user.token, logout)

  const attemptRestoreSession = () => {
    const userFromLocalStorage = localStorage.getItem(LOCAL_STORAGE_KEY)
    if (userFromLocalStorage) {
      setUser(JSON.parse(userFromLocalStorage))
    }
    setHasAttemptedRestoreSession(true)
  }
  useEffect(attemptRestoreSession, [])

  if (!hasAttemptedRestoreSession) { return null }

  return (
    <UserContext.Provider value={{ user, logout, login, ...authRequests }}>
      <BrowserRouter>
        <Nav />
        <h1>Applicant Submissions</h1>
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
      </BrowserRouter>    
    </UserContext.Provider>
  );
}

export default App;

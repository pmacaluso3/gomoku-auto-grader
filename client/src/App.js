import { BrowserRouter, Routes, Route } from "react-router-dom"
import { useEffect, useState } from "react";
import decode from "jwt-decode"

import './App.css';
import Nav from "./components/Nav";
import { buildAuthRequests } from "./utils/http"
import UserContext from "./contexts/UserContext"
import AppRoutes from "./components/AppRoutes";


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
        <AppRoutes />
      </BrowserRouter>    
    </UserContext.Provider>
  );
}

export default App;

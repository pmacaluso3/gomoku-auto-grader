const makeRequest = (route, method, body, token) => {
    const headers = {
        "Content-Type": "application/json",
        Accept: "application/json"
    }
    if (token) {
        headers.Authorization = `Bearer ${token}`
    }
    return fetch(process.env.REACT_APP_BACKEND_URL + route, {
        method: method,
        headers: headers,
        body: JSON.stringify(body)
    })
}

const post = (route, body, token) => {
    return makeRequest(route, "POST", body, token)
}

export {
    makeRequest,
    post
}
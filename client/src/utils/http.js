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

const get = (route, token) => {
    return makeRequest(route, "GET", null, token)
}

const post = (route, body, token) => {
    return makeRequest(route, "POST", body, token)
}

const put = (route, body, token) => {
    return makeRequest(route, "PUT", body, token)
}

const doDelete = (route, token) => {
    return makeRequest(route, "DELETE", null, token)
}

const buildAuthRequests = (token) => {
    const authGet = (route) => {
        return makeRequest(route, "GET", null, token)
    }
    
    const authPost = (route, body) => {
        return makeRequest(route, "POST", body, token)
    }
    
    const authPut = (route, body) => {
        return makeRequest(route, "PUT", body, token)
    }
    
    const authDelete = (route) => {
        return makeRequest(route, "DELETE", null, token)
    }

    return {
        authGet,
        authPost,
        authPut,
        authDelete
    }
}

export {
    makeRequest,
    get,
    post,
    put,
    doDelete,
    buildAuthRequests
}
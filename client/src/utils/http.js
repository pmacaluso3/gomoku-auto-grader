const makeRequest = ({ route, method, body, token }) => {
    const headers = {}
    const options = { method, headers }
    if (token) {
        headers.Authorization = `Bearer ${token}`
    }
    if ((body instanceof FormData)) {
        options.body = body
    } else {
        headers["Content-Type"] =  "application/json"
        headers.Accept = "application/json"
        options.body = body && JSON.stringify(body)
    }
    return fetch(process.env.REACT_APP_BACKEND_URL + route, options)
}

const get = (route, token) => {
    return makeRequest({ route, method: "GET", body: null, token })
}

const post = (route, body, token) => {
    return makeRequest({ route, method: "POST", body, token })
}

const put = (route, body, token) => {
    return makeRequest({ route, method: "PUT", body, token })
}

const doDelete = (route, token) => {
    return makeRequest({ route, method: "DELETE", body: null, token })
}

const buildAuthRequests = (token, forbiddenHandler) => {
    const useForbiddenHandler = (response) => {
        if (response.status === 403) {
            forbiddenHandler()
            return Promise.reject(new Error("Logged out"))
        } else {
            return response
        }
    }

    const authGet = (route) => {
        return get(route, token)
        .then(useForbiddenHandler)
    }
    
    const authPost = (route, body) => {
        return post(route, body, token)
        .then(useForbiddenHandler)
    }
    
    const authPut = (route, body) => {
        return put(route, body, token)
        .then(useForbiddenHandler)
    }
    
    const authDelete = (route) => {
        return doDelete(route, token)
        .then(useForbiddenHandler)
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
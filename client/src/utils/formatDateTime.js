const formatDateTime = (dateString) => {
    if (!dateString) { return null }

    const timeZone = Intl.DateTimeFormat().resolvedOptions().timeZone
    const date = new Date(dateString)
    // return `${date.getMonth() + 1}/${date.getDate()}/${date.getFullYear()} ${date.getHours()}:${date.getMinutes()}`
    return date.toLocaleString('en-US', { timeZone })
}

export default formatDateTime
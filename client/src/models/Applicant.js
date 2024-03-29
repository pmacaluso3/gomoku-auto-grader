import Record from "./Record";

export default class Applicant extends Record {
    constructor({ appUserId, username, firstName, lastName, accountSetupToken, hasBeenSetup }) {
        super()
        this.appUserId = appUserId
        this.username = username
        this.firstName = firstName
        this.lastName = lastName
        this.accountSetupToken = accountSetupToken
        this.hasBeenSetup = hasBeenSetup
    }

    setupComplete() {
        return this.hasBeenSetup ? "✅" : "❌"
    }

    userInfo() {
        return `${this.firstName} ${this.lastName} (username: ${this.username}, id: ${this.appUserId})`
    }
}
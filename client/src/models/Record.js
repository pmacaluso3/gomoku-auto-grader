export default class Record {
    getValue(key) {
        if (typeof this[key] === "function") {
            return this[key]()
        } else {
            return this[key]
        }
    }
}
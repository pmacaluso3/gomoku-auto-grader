export default class Record {
    accessCompositeKey(key) {
        const keyParts = key.split(".")
        let object = this
        for (const keyPart of keyParts) {
            object = object[keyPart]
        }
        return object
    }
    
    getValue(key) {
        const value = this.accessCompositeKey(key)
        if (typeof value === "function") {
            return value.bind(this)()
        } else {
            return value
        }
    }
}
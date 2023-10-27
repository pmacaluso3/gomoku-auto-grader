const humanize = (string) => {
    return string
      .replace(/_/g, ' ')
      .trim()
      .replace(/([a-z])([A-Z])/g, '$1 $2')
      .replace(/\b[A-Z][a-z]+\b/g, function(word) {
        return word.toLowerCase()
      })
      .replace(/^[a-z]/g, function(first) {
        return first.toUpperCase()
      })
}

export default humanize
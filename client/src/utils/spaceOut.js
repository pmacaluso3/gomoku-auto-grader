const spaceOut = (...args) => {
    const output = []

    args.forEach((arg, i) => {
        output.push(<span key={i}>{arg}</span>)
        if (i < args.length - 1) {
            output.push(" ")
        }
    }) 
    
    return output
}

export default spaceOut
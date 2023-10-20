const Table = ({ keys, records }) => {
    return (
        <table>
            <thead>
                <tr>
                    { keys && keys.map(k => <th key={`header-cell-${k}`}>{k}</th>)}
                </tr>
            </thead>
            <tbody>
                { records && records.map(r => <tr key={JSON.stringify(r)}>
                    { keys.map(k => <td key={JSON.stringify(r) + `-key-${k}`}>{ r[k] }</td>) }
                </tr>) }
            </tbody>
        </table>
    )
}

export default Table
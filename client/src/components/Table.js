const Table = ({ keys, records }) => {
    const getTdFromRecordAndKey = (record, key) => {
        const value = record.getValue(key)
        if (value && value.isHtml) {
            return <td key={JSON.stringify(record) + `-key-${key}`}  dangerouslySetInnerHTML={{ __html: value.value }} />
        } else {
            return <td key={JSON.stringify(record) + `-key-${key}`}>{value}</td>
        }
    }

    return (
        <table>
            <thead>
                <tr>
                    { keys && keys.map(k => <th key={`header-cell-${k}`}>{k}</th>)}
                </tr>
            </thead>
            <tbody>
                { records && records.map(r => <tr key={JSON.stringify(r)}>
                    { keys.map(k => getTdFromRecordAndKey(r, k)) }
                </tr>) }
            </tbody>
        </table>
    )
}

export default Table
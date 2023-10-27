import humanize from "../utils/humanize"

const Table = ({ keys, records }) => {
    const getTdFromRecordAndKey = (record, key) => {
        const value = record.getValue(key)
        if (value && value.isHtml) {
            return <td className="monospace" key={JSON.stringify(record) + `-key-${key}`}  dangerouslySetInnerHTML={{ __html: value.value }} />
        } else {
            return <td key={JSON.stringify(record) + `-key-${key}`}>{value}</td>
        }
    }

    return (
        <table className="table table-striped table-bordered table-hover">
            <thead>
                <tr>
                    { keys && keys.map(k => <th key={`header-cell-${k}`}>{humanize(k)}</th>)}
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
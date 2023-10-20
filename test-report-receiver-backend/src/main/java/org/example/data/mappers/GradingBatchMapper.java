package org.example.data.mappers;

import org.example.models.GradingBatch;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class GradingBatchMapper implements RowMapper<GradingBatch> {
    @Override
    public GradingBatch mapRow(ResultSet rs, int rowNum) throws SQLException {
        GradingBatch gradingBatch = new GradingBatch();
        gradingBatch.setGradingBatchId(rs.getInt("grading_batch_id"));
        gradingBatch.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
        return gradingBatch;
    }
}

package org.example.data.mappers;

import org.example.models.GradingBatch;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;

public class GradingBatchMapper implements RowMapper<GradingBatch> {
    @Override
    public GradingBatch mapRow(ResultSet rs, int rowNum) throws SQLException {
        GradingBatch gradingBatch = new GradingBatch();
        gradingBatch.setGradingBatchId(rs.getInt("grading_batch_id"));
        LocalDateTime createdAt;
        if (rs.getTimestamp("created_at") == null) {
            createdAt = null;
        } else {
            createdAt = rs.getTimestamp("created_at").toLocalDateTime();
        }
        gradingBatch.setCreatedAt(createdAt);
        return gradingBatch;
    }
}

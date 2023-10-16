package org.example.data.mappers;

import org.example.models.Submission;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class SubmissionMapper implements RowMapper<Submission> {

    @Override
    public Submission mapRow(ResultSet rs, int rowNum) throws SQLException {
        return new Submission(
            rs.getInt("submissionId"),
            rs.getInt("appUserId"),
            rs.getBlob("zipFile"),
            rs.getInt("gradingBatchId"),
            rs.getTimestamp("createdAt"),
            rs.getTimestamp("gradedAt")
        );
    }
}

package org.example.data.mappers;

import org.example.models.Submission;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class SubmissionMapper implements RowMapper<Submission> {

    @Override
    public Submission mapRow(ResultSet rs, int rowNum) throws SQLException {
        Submission submission = new Submission();

            submission.setSubmissionId(rs.getInt("submission_id"));
            submission.setAppUserId(rs.getInt("app_user_id"));
            submission.setZipFile(rs.getBlob("zip_file"));
            submission.setGradingBatchId(rs.getInt("grading_batch_id"));
            submission.setCreatedAt(rs.getTimestamp("created_at"));
            submission.setGradedAt(rs.getTimestamp("graded_at"));



//        return new Submission(
//            rs.getInt("submission_id"),
//            rs.getInt("app_user_id"),
//            rs.getBlob("zip_file"),
//            rs.getInt("grading_batch_id"),
//            rs.getTimestamp("created_at"),
//            rs.getTimestamp("graded_at")
//        );

        return submission;
    }
}

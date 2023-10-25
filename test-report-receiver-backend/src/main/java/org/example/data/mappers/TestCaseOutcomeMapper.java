package org.example.data.mappers;

import org.example.models.TestCaseOutcome;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class TestCaseOutcomeMapper implements RowMapper<TestCaseOutcome> {
    @Override
    public TestCaseOutcome mapRow(ResultSet rs, int rowNum) throws SQLException {
        return new TestCaseOutcome(
                rs.getInt("test_case_outcome_id"),
                rs.getString("test_name"),
                rs.getInt("submission_id"),
                rs.getBoolean("success"),
                rs.getBoolean("has_been_manually_edited"),
                rs.getString("description"),
                rs.getString("board_state")
        );
    }
}

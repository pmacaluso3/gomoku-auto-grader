package org.example.data;

import org.example.data.mappers.TestCaseOutcomeMapper;
import org.example.models.TestCaseOutcome;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;

@Repository
public class TestCaseOutcomeJdbcTemplateRepository implements TestCaseOutcomeRepository{

    private final JdbcTemplate jdbcTemplate;

    public TestCaseOutcomeJdbcTemplateRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public TestCaseOutcome create(TestCaseOutcome testCaseOutcome) {
        final String sql = "insert into test_case_outcome " +
                "(test_name, submission_id, success, description, board_state) " +
                "values (?, ?, ?, ?, ?);";

        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
        int rowsAffected = jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, testCaseOutcome.getTestName());
            ps.setInt(2, testCaseOutcome.getSubmissionId());
            ps.setBoolean(3, testCaseOutcome.isSuccess());
            ps.setString(4, testCaseOutcome.getDescription());
            ps.setString(5, testCaseOutcome.getBoardState());
            return ps;
        }, keyHolder);

        if (rowsAffected <= 0) {
            return null;
        }

        testCaseOutcome.setTestCaseOutcomeId(keyHolder.getKey().intValue());

        return testCaseOutcome;
    }

    @Override
    public boolean update(TestCaseOutcome testCaseOutcome) {
        String sql = "update test_case_outcome " +
                "set success = ?, has_been_manually_edited = true " +
                "where test_case_outcome_id = ?";
        return jdbcTemplate.update(sql,
                testCaseOutcome.isSuccess(),
                testCaseOutcome.getTestCaseOutcomeId()) > 0;
    }
}

package org.example.data;

import org.example.data.extractors.SubmissionWithTestCaseOutcomesAndAppUserExtractor;
import org.example.data.extractors.SubmissionWithTestCaseOutcomesExtractor;
import org.example.data.mappers.SubmissionMapper;
import org.example.models.Submission;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Repository
public class SubmissionJdbcTemplateRepository implements SubmissionRepository {

    private final JdbcTemplate jdbcTemplate;

    public SubmissionJdbcTemplateRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Submission> findAll() {
        String sql = "select s.*, tco.*, au.* from submission s " +
                "left outer join test_case_outcome tco on s.submission_id = tco.submission_id " +
                "join app_user au on s.app_user_id = au.app_user_id ";

        return jdbcTemplate.query(sql, new SubmissionWithTestCaseOutcomesAndAppUserExtractor());
    }

    @Override
    public List<Submission> findByApplicantUsername(String username) {
        String sql = "select s.*, tco.* " +
                "from submission s " +
                "join app_user au on s.app_user_id = au.app_user_id " +
                "left outer join test_case_outcome tco on s.submission_id = tco.submission_id " +
                "where au.username = ?";

        return jdbcTemplate.query(sql, new SubmissionWithTestCaseOutcomesExtractor(), username);
    }

    @Override
    public List<Submission> findByGradingBatchId(int id) {
        String sql = "select s.*, tco.* from submission s " +
                "left outer join test_case_outcome tco on s.submission_id = tco.submission_id where s.grading_batch_id = ?;";
        return jdbcTemplate.query(sql, new SubmissionWithTestCaseOutcomesExtractor(), id);
    }

    @Override
    public List<Submission> findWhereIdInList(List<Integer> ids) {
        String placeholders = String.join(",", Collections.nCopies(ids.size(), "?"));
        String sql = String.format("select * from submission where submission_id in (%s)", placeholders);
        return jdbcTemplate.query(sql, new SubmissionMapper(), ids.toArray());
    }

    @Override
    public Submission findById(int id) {
        String sql = "select s.*, tco.*, au.* from submission s " +
                "left outer join test_case_outcome tco on s.submission_id = tco.submission_id " +
                "join app_user au on s.app_user_id = au.app_user_id " +
                "where s.submission_id = ?";
        return jdbcTemplate.query(sql, new SubmissionWithTestCaseOutcomesAndAppUserExtractor(), id)
                .stream().findFirst().orElse(null);
    }

    @Override
    public Submission create(Submission submission) {
        String sql = "insert into submission (app_user_id, zip_file, created_at) values (?, ?, current_timestamp());";

        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
        int rowsAffected = jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setInt(1, submission.getAppUserId());
            ps.setBlob(2, submission.getZipFile());
            return ps;
        }, keyHolder);

        if (rowsAffected <= 0) {
            return null;
        }

        submission.setSubmissionId(keyHolder.getKey().intValue());

        return submission;
    }

    @Override
    public Submission markGraded(Submission submission) {
        String sql = "update submission set grading_batch_id = ?, graded_at = current_timestamp() " +
                "where submission.submission_id = ?";
        jdbcTemplate.update(sql, submission.getGradingBatchId(), submission.getSubmissionId());
        String confirmationSql = "select * from submission where submission_id = ?";
        return jdbcTemplate.query(confirmationSql, new SubmissionMapper(), submission.getSubmissionId())
                .stream().findFirst().orElse(null);
    }

    @Override
    public boolean archive(int id) {
        String sql = "update submission set archived = true where submission_id = ?";
        return jdbcTemplate.update(sql, id) > 0;
    }
}

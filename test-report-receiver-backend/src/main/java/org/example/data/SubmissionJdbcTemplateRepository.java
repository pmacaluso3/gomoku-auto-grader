package org.example.data;

import org.example.data.mappers.SubmissionMapper;
import org.example.models.Submission;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;

@Repository
public class SubmissionJdbcTemplateRepository implements SubmissionRepository {

    private final JdbcTemplate jdbcTemplate;

    public SubmissionJdbcTemplateRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Submission> findAll() {
        String sql = "select * from submission";

        return jdbcTemplate.query(sql, new SubmissionMapper());
    }

    @Override
    public List<Submission> findByApplicantUsername(String username) {
        String sql = "select s." +
                "* from submission s " +
                "join app_user au on s.app_user_id = au.app_user_id " +
                "where au.username = ?";

        return jdbcTemplate.query(sql, new SubmissionMapper(), username);
    }

    @Override
    public List<Submission> findByGradingBatchId(int id) {
        String sql = "select * from submission where grading_batch_id = ?;";
        return jdbcTemplate.query(sql, new SubmissionMapper(), id);
    }

    @Override
    public Submission create(Submission submission) {
        final String sql = "insert into submission (app_user_id, zip_file, created_at) values (?, ?, current_timestamp());";

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
    public boolean update(Submission submission) {
        return false;
    }
}

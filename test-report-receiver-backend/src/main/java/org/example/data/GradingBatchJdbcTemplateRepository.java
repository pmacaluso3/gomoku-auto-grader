package org.example.data;

import org.example.data.mappers.GradingBatchMapper;
import org.example.models.GradingBatch;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;

@Repository
public class GradingBatchJdbcTemplateRepository implements GradingBatchRepository {

    private final JdbcTemplate jdbcTemplate;

    public GradingBatchJdbcTemplateRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<GradingBatch> findAll() {
        String sql = "select * from grading_batch";
        return jdbcTemplate.query(sql, new GradingBatchMapper());
    }

    @Override
    public GradingBatch findById(int id) {
        String sql = "select * from grading_batch where grading_batch_id = ?";
        return jdbcTemplate.query(sql, new GradingBatchMapper(), id).stream().findFirst().orElse(null);
    }

    @Override
    public GradingBatch create(GradingBatch gradingBatch) {
        final String sql = "insert into grading_batch (created_at) values (current_timestamp())";

        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
        int rowsAffected = jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            return ps;
        }, keyHolder);

        if (rowsAffected <= 0) {
            return null;
        }

        gradingBatch.setGradingBatchId(keyHolder.getKey().intValue());
        return gradingBatch;
    }
}

package org.example.data.extractors;

import org.example.models.AppUser;
import org.example.models.Submission;
import org.example.models.TestCaseOutcome;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;

public class SubmissionWithTestCaseOutcomesAndAppUserExtractor implements ResultSetExtractor<List<Submission>> {
    @Override
    public List<Submission> extractData(ResultSet rs) throws SQLException, DataAccessException {
        Map<Integer, Submission> submissionsMap = new HashMap<>();

        while (rs.next()) {
            Integer submissionId = rs.getInt("s.submission_id");
            Submission submission = submissionsMap.get(submissionId);
            if (submission == null) {
                submission = new Submission();

                submission.setSubmissionId(rs.getInt("s.submission_id"));
                submission.setAppUserId(rs.getInt("s.app_user_id"));
                submission.setZipFile(rs.getBlob("s.zip_file"));
                submission.setGradingBatchId(rs.getInt("s.grading_batch_id"));
                submission.setCreatedAt(rs.getTimestamp("s.created_at"));
                submission.setGradedAt(rs.getTimestamp("s.graded_at"));

                AppUser appUser = new AppUser();
                appUser.setUsername(rs.getString("au.username"));
                appUser.setFirstName(rs.getString("au.first_name"));
                appUser.setLastName(rs.getString("au.last_name"));
                appUser.setAppUserId(rs.getInt("au.app_user_id"));
                submission.setAppUser(appUser);

                submissionsMap.put(submissionId, submission);
            }

            if (submission.getGradedAt() != null) {
                TestCaseOutcome testCaseOutcome = new TestCaseOutcome(
                        rs.getInt("tco.test_case_outcome_id"),
                        rs.getString("tco.test_name"),
                        rs.getInt("tco.submission_id"),
                        rs.getBoolean("tco.success"),
                        rs.getBoolean("tco.has_been_manually_edited"),
                        rs.getString("tco.description"),
                        rs.getString("tco.board_state")
                );
                submissionsMap.get(submissionId).addTestCaseOutcome(testCaseOutcome);
            }
        }

        return submissionsMap.entrySet().stream().map(e -> e.getValue()).collect(Collectors.toList());
    }
}

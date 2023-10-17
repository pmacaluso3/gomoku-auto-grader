package org.example.data;

import org.example.models.TestCaseOutcome;

import java.util.List;

public interface TestCaseOutcomeRepository {
    public TestCaseOutcome create(TestCaseOutcome testCaseOutcome);

    public boolean update(TestCaseOutcome testCaseOutcome);
}

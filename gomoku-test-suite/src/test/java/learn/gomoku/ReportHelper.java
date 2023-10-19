package learn.gomoku;

import org.junit.jupiter.api.TestInfo;

import java.util.function.Consumer;

public class ReportHelper {

    private final TestResultReportClient reportClient;

    public ReportHelper(TestResultReportClient reportClient) {
        this.reportClient = reportClient;
    }

    public void reportTestCases(Consumer<Void> testCases, TestInfo testInfo) {
        String testName = getTestName(testInfo);
        try {
            testCases.accept(null);
            reportClient.report( testName, true, null, null);
        } catch (AssertionError ex) {
            reportClient.report(testName, false, ex.getMessage(), null);
        }
    }

    private String getTestName(TestInfo testInfo) {
        return testInfo.getDisplayName().replace("(TestInfo)", "");
    }
}

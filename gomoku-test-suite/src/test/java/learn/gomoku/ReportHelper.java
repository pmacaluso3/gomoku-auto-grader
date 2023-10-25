package learn.gomoku;

import org.junit.jupiter.api.TestInfo;

import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

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
        } catch (Exception ex) {
            reportClient.report(testName, false, ex.getMessage(), null);
        }
    }

    public void reportBoardState(Function<Void, String> testCases, TestInfo testInfo ) {
        String testName = getTestName(testInfo);
        try {
            String boardState = testCases.apply(null);
            reportClient.report(testName, true, null, boardState);
        } catch (Exception ex) {
            reportClient.report(testName, false, ex.getMessage(), "n/a");
        }
    }

    private String getTestName(TestInfo testInfo) {
        return testInfo.getDisplayName().replace("(TestInfo)", "");
    }
}

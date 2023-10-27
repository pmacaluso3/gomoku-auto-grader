package learn.gomoku;

import learn.gomoku.game.Gomoku;
import learn.gomoku.players.HumanPlayer;
import learn.gomoku.players.RandomPlayer;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

class AppTest {

    private final String PATH_TO_SCRIPTS = "./src/test/java/learn/gomoku/scripts/";

    private final int SEED = generateSeedThatDoesNotInterfereWithFirstFiveHumanMovesOrProduceDuplicates();

    private final char BLANK_SPACE_CHAR = '_';

    private final char BLACK_STONE_CHAR = 'B';

    private final char WHITE_STONE_CHAR = 'W';

    private final ReportHelper reportHelper = new ReportHelper(new TestResultReportClient());

    @Nested
    class PlayerSelection {
        @Test
        void selectPlayersHumanVsHuman(TestInfo testInfo) {
            reportHelper.reportTestCases((unused) -> {
                Gomoku game = loadScript("playerSelection/selectPlayersHumanVsHuman");

                assertEquals(HumanPlayer.class, game.getPlayerOne().getClass());
                assertEquals(HumanPlayer.class, game.getPlayerTwo().getClass());
                assertEquals("Player1", game.getPlayerOne().getName());
                assertEquals("Player2", game.getPlayerTwo().getName());
            }, testInfo);
        }

        @Test
        void selectPlayersHumanVsRandom(TestInfo testInfo) {
            reportHelper.reportTestCases((unused) -> {
                Gomoku game = loadScript("playerSelection/selectPlayersHumanVsRandom");

                assertEquals(HumanPlayer.class, game.getPlayerOne().getClass());
                assertEquals(RandomPlayer.class, game.getPlayerTwo().getClass());
                assertEquals("Player1", game.getPlayerOne().getName());
            }, testInfo);
        }

        @Test
        void selectPlayersRandomVsHuman(TestInfo testInfo) {
            reportHelper.reportTestCases((unused) -> {
                Gomoku game = loadScript("playerSelection/selectPlayersRandomVsHuman");

                assertEquals(RandomPlayer.class, game.getPlayerOne().getClass());
                assertEquals(HumanPlayer.class, game.getPlayerTwo().getClass());
                assertEquals("Player2", game.getPlayerTwo().getName());
            }, testInfo);
        }

        @Test
        void selectPlayersRandomVsRandom(TestInfo testInfo) {
            reportHelper.reportTestCases((unused) -> {
                Gomoku game = loadScript("playerSelection/selectPlayersRandomVsRandom");

                assertEquals(RandomPlayer.class, game.getPlayerOne().getClass());
                assertEquals(RandomPlayer.class, game.getPlayerTwo().getClass());
            }, testInfo);
        }
    }

    @Nested
    class SingleMoves {
        @Test
        void validHumanMove(TestInfo testInfo) {
            ByteArrayOutputStream stdout = captureStdOut();

            reportHelper.reportTestCases((unused) -> {
                Gomoku game = loadScript("singleMoves/validHumanMove");

                assertTrue(game.getStones().get(0).getRow() == 0 && game.getStones().get(0).getColumn() == 0);
                assertFalse(stdout.toString().contains(Gomoku.OFF_BOARD_MESSAGE));
                assertFalse(stdout.toString().contains(Gomoku.DUPLICATE_MOVE_MESSAGE));
            }, testInfo);
        }

        @Test
        void offBoardHumanMove(TestInfo testInfo) {
            ByteArrayOutputStream stdout = captureStdOut();

            reportHelper.reportTestCases((unused) -> {
                Gomoku game = loadScript("singleMoves/offBoardHumanMove");

                assertTrue(game.getStones().isEmpty());
                assertTrue(stdout.toString().contains(Gomoku.OFF_BOARD_MESSAGE));
                assertFalse(stdout.toString().contains(Gomoku.DUPLICATE_MOVE_MESSAGE));
            }, testInfo);
        }

        @Test
        void duplicateHumanMove(TestInfo testInfo) {
            ByteArrayOutputStream stdout = captureStdOut();

            reportHelper.reportTestCases((unused) -> {
                Gomoku game = loadScript("singleMoves/duplicateHumanMove");

                assertEquals(1, game.getStones().size());
                assertTrue(game.getStones().get(0).getRow() == 0 && game.getStones().get(0).getColumn() == 0);
                assertTrue(stdout.toString().contains(Gomoku.DUPLICATE_MOVE_MESSAGE));
                assertFalse(stdout.toString().contains(Gomoku.OFF_BOARD_MESSAGE));
            }, testInfo);
        }

        @Test
        void twoHumanMoves(TestInfo testInfo) {
            ByteArrayOutputStream stdout = captureStdOut();

            reportHelper.reportTestCases((unused) -> {
                Gomoku game = loadScript("singleMoves/twoHumanMoves");

                assertEquals(2, game.getStones().size());
                assertTrue(game.getStones().get(0).getRow() == 0 && game.getStones().get(0).getColumn() == 0);
                assertTrue(game.getStones().get(1).getRow() == 1 && game.getStones().get(0).getColumn() == 0);
                assertFalse(stdout.toString().contains(Gomoku.DUPLICATE_MOVE_MESSAGE));
                assertFalse(stdout.toString().contains(Gomoku.OFF_BOARD_MESSAGE));
            }, testInfo);
        }

        // TODO: test random + human?
        // TODO: test random off-board?
    }

    @Nested
    class BoardPrinting {
        @Test
        void afterOneHumanMove(TestInfo testInfo) {
            ByteArrayOutputStream stdout = captureStdOut();

            reportHelper.reportBoardState((unused) -> {
                loadScript("boardPrinting/afterOneHumanMove");
                return stdout.toString();
            }, testInfo);
        }

        @Test
        void afterTwoHumanMoves(TestInfo testInfo) {
            ByteArrayOutputStream stdout = captureStdOut();

            reportHelper.reportBoardState((unused) -> {
                loadScript("boardPrinting/afterTwoHumanMoves");
                return stdout.toString();
            }, testInfo);
        }
    }

    @Nested
    class FullGames {
        @Test
        void humanVsHumanSingleGame(TestInfo testInfo) {
            reportHelper.reportTestCases((unused) -> {
                Gomoku game = loadScript("fullGames/humanVsHumanSingleGame");

                assertEquals(9, game.getStones().size());
                assertEquals(5, game.getStones().stream().filter(s -> s.isBlack()).collect(Collectors.toList()).size());
                assertEquals(4, game.getStones().stream().filter(s -> !s.isBlack()).collect(Collectors.toList()).size());
                assertTrue(game.isOver());
            }, testInfo);
        }

        @Test
        void humanVsHumanRematch(TestInfo testInfo) {
            reportHelper.reportTestCases((unused) -> {
                Gomoku game = loadScript("fullGames/humanVsHumanRematch");

                assertFalse(game.isOver());
            }, testInfo);
        }

        @Test
        void humanVsRandomSingleGame(TestInfo testInfo) {
            RandomPlayer.random = new Random(SEED);

            reportHelper.reportTestCases((unused) -> {
                Gomoku game = loadScript("fullGames/humanVsRandomSingleGame");

                assertTrue(game.getStones().size() == 9 || game.getStones().size() == 10);
                assertTrue(game.isOver());
                assertEquals(game.getWinner(), game.getPlayerOne());
            }, testInfo);
        }

        @Test
        void humanVsRandomRematch(TestInfo testInfo) {
            reportHelper.reportTestCases((unused) -> {
                Gomoku game = loadScript("fullGames/humanVsRandomRematch");

                assertFalse(game.isOver());
            }, testInfo);
        }

        @Test
        void randomVsRandomSingleGame(TestInfo testInfo) {
            reportHelper.reportTestCases((unused) -> {
                Gomoku game = loadScript("fullGames/randomVsRandomSingleGame");

                assertTrue(game.isOver());
            }, testInfo);
        }

        @Test
        void randomVsRandomRematch(TestInfo testInfo) {
            // if rematch is also randomVsRandom, no way to tell if the game is over because the first game ended
            // or if it's over because the second game also ended
            reportHelper.reportTestCases((unused) -> {
                Gomoku game = loadScript("fullGames/randomVsRandomRematch");

                assertFalse(game.isOver());
            }, testInfo);
        }
    }

    private Gomoku loadScript(String scriptName) {
        try {
            System.setIn(new FileInputStream(new File(PATH_TO_SCRIPTS + scriptName + ".txt")));
            String[] args = {};
            App.main(args);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (NoSuchElementException ex) {} // this happens when it reaches the end of the script
        return Gomoku.getInstance();
    }

    private ByteArrayOutputStream captureStdOut() {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        System.setOut(new PrintStream(baos));
        return baos;
    }

    private long countInstancesOfCharacterInString(char character, String string) {
        return string.chars().filter(c -> c == character).count();
    }

    private int generateSeedThatDoesNotInterfereWithFirstFiveHumanMovesOrProduceDuplicates() {
        int seed = 0;
        List<Integer> moves = new ArrayList<>();
        do {
            seed ++;
            Random random = new Random(seed);
            moves = new ArrayList<>();
            for (int i = 0; i < 10; i++) {
                moves.add(random.nextInt(15));
            }
        } while (moves.stream().anyMatch(i -> i <= 5) || moves.size() == new HashSet<Integer>(moves).size());
        return seed;
    }
}
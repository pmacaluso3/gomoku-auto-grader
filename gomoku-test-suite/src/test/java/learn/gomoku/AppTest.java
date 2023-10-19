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

    private final char BLANK_SPACE_CHAR = '☐';

    private final char BLACK_STONE_CHAR = '⚫';

    private final char WHITE_STONE_CHAR = '⚪';

    private final ReportHelper reportHelper = new ReportHelper(new TestResultReportClient());

    @Nested
    class PlayerSelection {
        @Test
        void selectPlayersHumanVsHuman(TestInfo testInfo) {
            Gomoku game = loadScript("playerSelection/selectPlayersHumanVsHuman");

            reportHelper.reportTestCases((unused) -> {
                assertEquals(HumanPlayer.class, game.getPlayerOne().getClass());
                assertEquals(HumanPlayer.class, game.getPlayerTwo().getClass());
                assertEquals("Player1", game.getPlayerOne().getName());
                assertEquals("Player2", game.getPlayerTwo().getName());
            }, testInfo);
        }

        @Test
        void selectPlayersHumanVsRandom(TestInfo testInfo) {
            Gomoku game = loadScript("playerSelection/selectPlayersHumanVsRandom");

            reportHelper.reportTestCases((unused) -> {
                assertEquals(HumanPlayer.class, game.getPlayerOne().getClass());
                assertEquals(RandomPlayer.class, game.getPlayerTwo().getClass());
                assertEquals("Player1", game.getPlayerOne().getName());
            }, testInfo);
        }

        @Test
        void selectPlayersRandomVsHuman(TestInfo testInfo) {
            Gomoku game = loadScript("playerSelection/selectPlayersRandomVsHuman");

            reportHelper.reportTestCases((unused) -> {
                assertEquals(RandomPlayer.class, game.getPlayerOne().getClass());
                assertEquals(HumanPlayer.class, game.getPlayerTwo().getClass());
                assertEquals("Player2", game.getPlayerTwo().getName());
            }, testInfo);
        }

        @Test
        void selectPlayersRandomVsRandom(TestInfo testInfo) {
            Gomoku game = loadScript("playerSelection/selectPlayersRandomVsRandom");

            reportHelper.reportTestCases((unused) -> {
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

            Gomoku game = loadScript("singleMoves/validHumanMove");

            reportHelper.reportTestCases((unused) -> {
                assertTrue(game.getStones().get(0).getRow() == 0 && game.getStones().get(0).getColumn() == 0);
                assertFalse(stdout.toString().contains(Gomoku.OFF_BOARD_MESSAGE));
                assertFalse(stdout.toString().contains(Gomoku.DUPLICATE_MOVE_MESSAGE));
            }, testInfo);
        }

        @Test
        void offBoardHumanMove(TestInfo testInfo) {
            ByteArrayOutputStream stdout = captureStdOut();

            Gomoku game = loadScript("singleMoves/offBoardHumanMove");

            reportHelper.reportTestCases((unused) -> {
                assertTrue(game.getStones().isEmpty());
                assertTrue(stdout.toString().contains(Gomoku.OFF_BOARD_MESSAGE));
                assertFalse(stdout.toString().contains(Gomoku.DUPLICATE_MOVE_MESSAGE));
            }, testInfo);
        }

        @Test
        void duplicateHumanMove(TestInfo testInfo) {
            ByteArrayOutputStream stdout = captureStdOut();

            Gomoku game = loadScript("singleMoves/duplicateHumanMove");

            reportHelper.reportTestCases((unused) -> {
                assertEquals(1, game.getStones().size());
                assertTrue(game.getStones().get(0).getRow() == 0 && game.getStones().get(0).getColumn() == 0);
                assertTrue(stdout.toString().contains(Gomoku.DUPLICATE_MOVE_MESSAGE));
                assertFalse(stdout.toString().contains(Gomoku.OFF_BOARD_MESSAGE));
            }, testInfo);
        }

        @Test
        void twoHumanMoves(TestInfo testInfo) {
            ByteArrayOutputStream stdout = captureStdOut();

            Gomoku game = loadScript("singleMoves/twoHumanMoves");

            reportHelper.reportTestCases((unused) -> {
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

            Gomoku game = loadScript("boardPrinting/afterOneHumanMove");

//            String expectedBoardState = boardState("afterOneHumanMove").replaceAll("\\s", "");
//            String actualBoardState = stdout.toString().replaceAll("\\s", "");

            // WIP: what's the mismatch here
//            assertTrue(stdout.toString().replaceAll("\\s", "").contains(boardState("afterOneHumanMove").replaceAll("//s", "")));

            reportHelper.reportTestCases((unused) -> {
                int numberOfBlanks = (15 * 15) - 1;
                assertEquals(numberOfBlanks, countInstancesOfCharacterInString(BLANK_SPACE_CHAR, stdout.toString()));
                assertEquals(1, countInstancesOfCharacterInString(BLACK_STONE_CHAR, stdout.toString()));
                assertTrue(stdout.toString().contains("01 02 03 04 05 06 07 08 09 10 11 12 13 14 15"));
            }, testInfo);

            // TODO: report board state here
        }

        @Test
        void afterTwoHumanMoves(TestInfo testInfo) {
            ByteArrayOutputStream stdout = captureStdOut();

            Gomoku game = loadScript("boardPrinting/afterTwoHumanMoves");

            reportHelper.reportTestCases((unused) -> {
                int numberOfBlanks = ((15 * 15) - 1) + ((15 * 15 - 2)); // two printed boards
                assertEquals(numberOfBlanks, countInstancesOfCharacterInString(BLANK_SPACE_CHAR, stdout.toString()));
                assertEquals(2, countInstancesOfCharacterInString(BLACK_STONE_CHAR, stdout.toString()));
                assertEquals(1, countInstancesOfCharacterInString(WHITE_STONE_CHAR, stdout.toString()));
            }, testInfo);

            // TODO: report board state here
        }
    }

    @Nested
    class FullGames {
        @Test
        void humanVsHumanSingleGame(TestInfo testInfo) {
            Gomoku game = loadScript("fullGames/humanVsHumanSingleGame");

            reportHelper.reportTestCases((unused) -> {
                assertEquals(9, game.getStones().size());
                assertEquals(5, game.getStones().stream().filter(s -> s.isBlack()).collect(Collectors.toList()).size());
                assertEquals(4, game.getStones().stream().filter(s -> !s.isBlack()).collect(Collectors.toList()).size());
                assertTrue(game.isOver());
            }, testInfo);
        }

        @Test
        void humanVsHumanRematch(TestInfo testInfo) {
            Gomoku game = loadScript("fullGames/humanVsHumanRematch");

            reportHelper.reportTestCases((unused) -> {
                assertFalse(game.isOver());
            }, testInfo);
        }

        @Test
        void humanVsRandomSingleGame(TestInfo testInfo) {
            RandomPlayer.random = new Random(SEED);

            Gomoku game = loadScript("fullGames/humanVsRandomSingleGame");

            reportHelper.reportTestCases((unused) -> {
                assertTrue(game.getStones().size() == 9 || game.getStones().size() == 10);
                assertTrue(game.isOver());
                assertEquals(game.getWinner(), game.getPlayerOne());
            }, testInfo);
        }

        @Test
        void humanVsRandomRematch(TestInfo testInfo) {
            Gomoku game = loadScript("fullGames/humanVsRandomRematch");

            reportHelper.reportTestCases((unused) -> {
                assertFalse(game.isOver());
            }, testInfo);
        }

        @Test
        void randomVsRandomSingleGame(TestInfo testInfo) {
            Gomoku game = loadScript("fullGames/randomVsRandomSingleGame");

            reportHelper.reportTestCases((unused) -> {
                assertTrue(game.isOver());
            }, testInfo);
        }

        @Test
        void randomVsRandomRematch(TestInfo testInfo) {
            Gomoku game = loadScript("fullGames/randomVsRandomRematch");

            // if rematch is also randomVsRandom, no way to tell if the game is over because the first game ended
            // or if it's over because the second game also ended
            reportHelper.reportTestCases((unused) -> {
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
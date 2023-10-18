package learn.gomoku;

import learn.gomoku.game.Gomoku;
import learn.gomoku.players.HumanPlayer;
import learn.gomoku.players.RandomPlayer;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

class AppTest {

    private final String PATH_TO_SCRIPTS = "./src/test/java/learn/gomoku/scripts/";

    private final String PATH_TO_BOARDS = "./src/test/java/learn/gomoku/expectedBoards/";

    private final int SEED = generateSeedThatDoesNotInterfereWithFirstFiveHumanMovesOrProduceDuplicates();

    private final char BLANK_SPACE_CHAR = '☐';

    private final char BLACK_STONE_CHAR = '⚫';

    private final char WHITE_STONE_CHAR = '⚪';

    private final TestResultReportClient reportClient = new TestResultReportClient();

    @Nested
    class PlayerSelection {
        @Test
        void selectPlayersHumanVsHuman() {
            Gomoku game = loadScript("playerSelection/selectPlayersHumanVsHuman");

            reportAssertEquals(HumanPlayer.class, game.getPlayerOne().getClass());
            reportAssertEquals(HumanPlayer.class, game.getPlayerTwo().getClass());
            reportAssertEquals("Player1", game.getPlayerOne().getName());
            reportAssertEquals("Player2", game.getPlayerTwo().getName());
        }

        @Test
        void selectPlayersHumanVsRandom() {
            Gomoku game = loadScript("playerSelection/selectPlayersHumanVsRandom");

            assertEquals(HumanPlayer.class, game.getPlayerOne().getClass());
            assertEquals(RandomPlayer.class, game.getPlayerTwo().getClass());
            assertEquals("Player1", game.getPlayerOne().getName());
        }

        @Test
        void selectPlayersRandomVsHuman() {
            Gomoku game = loadScript("playerSelection/selectPlayersRandomVsHuman");

            assertEquals(RandomPlayer.class, game.getPlayerOne().getClass());
            assertEquals(HumanPlayer.class, game.getPlayerTwo().getClass());
            assertEquals("Player2", game.getPlayerTwo().getName());
        }

        @Test
        void selectPlayersRandomVsRandom() {
            Gomoku game = loadScript("playerSelection/selectPlayersRandomVsRandom");

            assertEquals(RandomPlayer.class, game.getPlayerOne().getClass());
            assertEquals(RandomPlayer.class, game.getPlayerTwo().getClass());
        }
    }

    @Nested
    class SingleMoves {
        @Test
        void validHumanMove() {
            ByteArrayOutputStream stdout = captureStdOut();

            Gomoku game = loadScript("singleMoves/validHumanMove");

            assertTrue(game.getStones().get(0).getRow() == 0 && game.getStones().get(0).getColumn() == 0);
            assertFalse(stdout.toString().contains(Gomoku.OFF_BOARD_MESSAGE));
            assertFalse(stdout.toString().contains(Gomoku.DUPLICATE_MOVE_MESSAGE));
        }

        @Test
        void offBoardHumanMove() {
            ByteArrayOutputStream stdout = captureStdOut();

            Gomoku game = loadScript("singleMoves/offBoardHumanMove");

            assertTrue(game.getStones().isEmpty());
            assertTrue(stdout.toString().contains(Gomoku.OFF_BOARD_MESSAGE));
            assertFalse(stdout.toString().contains(Gomoku.DUPLICATE_MOVE_MESSAGE));
        }

        @Test
        void duplicateHumanMove() {
            ByteArrayOutputStream stdout = captureStdOut();

            Gomoku game = loadScript("singleMoves/duplicateHumanMove");

            assertEquals(1, game.getStones().size());
            assertTrue(game.getStones().get(0).getRow() == 0 && game.getStones().get(0).getColumn() == 0);
            assertTrue(stdout.toString().contains(Gomoku.DUPLICATE_MOVE_MESSAGE));
            assertFalse(stdout.toString().contains(Gomoku.OFF_BOARD_MESSAGE));
        }

        @Test
        void twoHumanMoves() {
            ByteArrayOutputStream stdout = captureStdOut();

            Gomoku game = loadScript("singleMoves/twoHumanMoves");

            assertEquals(2, game.getStones().size());
            assertTrue(game.getStones().get(0).getRow() == 0 && game.getStones().get(0).getColumn() == 0);
            assertTrue(game.getStones().get(1).getRow() == 1 && game.getStones().get(0).getColumn() == 0);
            assertFalse(stdout.toString().contains(Gomoku.DUPLICATE_MOVE_MESSAGE));
            assertFalse(stdout.toString().contains(Gomoku.OFF_BOARD_MESSAGE));
        }

        // TODO: test random + human?
        // TODO: test random off-board?
    }

    @Nested
    class BoardPrinting {
        @Test
        void afterOneHumanMove() {
            ByteArrayOutputStream stdout = captureStdOut();

            Gomoku game = loadScript("boardPrinting/afterOneHumanMove");

//            String expectedBoardState = boardState("afterOneHumanMove").replaceAll("\\s", "");
//            String actualBoardState = stdout.toString().replaceAll("\\s", "");

            // WIP: what's the mismatch here
//            assertTrue(stdout.toString().replaceAll("\\s", "").contains(boardState("afterOneHumanMove").replaceAll("//s", "")));
            int numberOfBlanks = (15 * 15) - 1;
            assertEquals(numberOfBlanks, countInstancesOfCharacterInString(BLANK_SPACE_CHAR, stdout.toString()));
            assertEquals(1, countInstancesOfCharacterInString(BLACK_STONE_CHAR, stdout.toString()));
            assertTrue(stdout.toString().contains("01 02 03 04 05 06 07 08 09 10 11 12 13 14 15"));
        }

        @Test
        void afterTwoHumanMoves() {
            ByteArrayOutputStream stdout = captureStdOut();

            Gomoku game = loadScript("boardPrinting/afterTwoHumanMoves");

            int numberOfBlanks = ((15 * 15) - 1) + ((15 * 15 - 2)); // two printed boards
            assertEquals(numberOfBlanks, countInstancesOfCharacterInString(BLANK_SPACE_CHAR, stdout.toString()));
            assertEquals(2, countInstancesOfCharacterInString(BLACK_STONE_CHAR, stdout.toString()));
            assertEquals(1, countInstancesOfCharacterInString(WHITE_STONE_CHAR, stdout.toString()));
        }
    }

    @Nested
    class FullGames {
        @Test
        void humanVsHumanSingleGame() {
            Gomoku game = loadScript("fullGames/humanVsHumanSingleGame");

            assertEquals(9, game.getStones().size());
            assertEquals(5, game.getStones().stream().filter(s -> s.isBlack()).collect(Collectors.toList()).size());
            assertEquals(4, game.getStones().stream().filter(s -> !s.isBlack()).collect(Collectors.toList()).size());
            assertTrue(game.isOver());
        }

        @Test
        void humanVsHumanRematch() {
            Gomoku game = loadScript("fullGames/humanVsHumanRematch");

            assertFalse(game.isOver());
        }

        @Test
        void humanVsRandomSingleGame() {
            RandomPlayer.random = new Random(SEED);

            Gomoku game = loadScript("fullGames/humanVsRandomSingleGame");

            assertTrue(game.getStones().size() == 9 || game.getStones().size() == 10);
            assertTrue(game.isOver());
            assertEquals(game.getWinner(), game.getPlayerOne());
        }

        @Test
        void humanVsRandomRematch() {
            Gomoku game = loadScript("fullGames/humanVsRandomRematch");

            assertFalse(game.isOver());
        }

        @Test
        void randomVsRandomSingleGame() {
            Gomoku game = loadScript("fullGames/randomVsRandomSingleGame");

            assertTrue(game.isOver());
        }

        @Test
        void randomVsRandomRematch() {
            Gomoku game = loadScript("fullGames/randomVsRandomRematch");

            // if rematch is also randomVsRandom, no way to tell if the game is over because the first game ended
            // or if it's over because the second game also ended
            assertFalse(game.isOver());
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

    private String boardState(String filename) {
        try {
            return Files.readString(Paths.get(PATH_TO_BOARDS + filename + ".txt"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
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

    private void reportAssertEquals(Object expected, Object actual) {
        try {
            assertEquals(expected, actual);
            reportClient.report(true);
        } catch (AssertionError ex) {
            reportClient.report(false);
        }
    }
}
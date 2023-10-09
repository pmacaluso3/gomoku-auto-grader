package learn.gomoku;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class FilesUnchangedTest {

    private final Map<String, Integer> fileHashes;

    private final String PATH_TO_PACKAGE = "./src/main/java/learn/gomoku/";

    private final List<String> UNCHANGEABLE_FILES = List.of(
            "game/Gomoku",
            "game/Result",
            "game/Stone",
            "players/HumanPlayer",
            "players/Player",
            "players/RandomPlayer"
    );

    public FilesUnchangedTest() {
        this.fileHashes = new HashMap<>();
        fileHashes.put("game/Gomoku", -859978494);
        fileHashes.put("game/Result", -1480996043);
        fileHashes.put("game/Stone", -1365582593);
        fileHashes.put("players/HumanPlayer", 1692853361);
        fileHashes.put("players/Player", 1965106920);
        fileHashes.put("players/RandomPlayer", -1330107864);

    }

    @Test
    void shouldNotHaveChangedStarterFiles() {
        fileHashes.forEach((filename, hashInteger) -> {
            try {
                assertEquals(hashInteger, Files.readString(Paths.get(PATH_TO_PACKAGE + filename + ".java")).hashCode());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }

//    @Test
//    void generateHashCodes() throws IOException {
//        for (String filename : UNCHANGEABLE_FILES) {
//            String fileContents = Files.readString(Paths.get(PATH_TO_PACKAGE + filename + ".java"));
//            System.out.printf("fileHashes.put(\"%s\", %s);", filename, fileContents.hashCode());
//            System.out.println("");
//        }
//    }
}

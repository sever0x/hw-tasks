package com.sever0x.block1.parser;

import com.sever0x.block1.model.Song;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.List;
import java.util.concurrent.ExecutionException;

import static org.junit.jupiter.api.Assertions.*;

class JsonPlaylistParserTest {
    private JsonPlaylistParser parser;
    private final String testDirectoryPath = "src/test/resources/json";

    @BeforeEach
    void setup() {
        parser = new JsonPlaylistParser(2);
    }

    @Test
    void testParsePlaylistFromDirectory() {
        assertDoesNotThrow(() -> {
            List<Song> songs = parser.parsePlaylistFromDirectory(testDirectoryPath);
            assertFalse(songs.isEmpty());
        });
    }

    @Test
    void testParsePlaylistFromDirectoryWithInvalidPath() {
        assertThrows(IllegalArgumentException.class, () -> {
            parser.parsePlaylistFromDirectory("");
        });
        assertThrows(IllegalArgumentException.class, () -> {
            parser.parsePlaylistFromDirectory(null);
        });
        assertThrows(IllegalArgumentException.class, () -> {
            parser.parsePlaylistFromDirectory("invalid/path");
        });
    }

    @Test
    void testParseSongsFromFile() {
        File file = new File(testDirectoryPath + "/favorites.json");
        assertDoesNotThrow(() -> {
            List<Song> songs = parser.parseSongsFromFile(file);
            assertFalse(songs.isEmpty());
        });
    }

    @Test
    void testParseSongsFromFileWithInvalidFile() {
        File file = new File(testDirectoryPath + "/invalid.json");
        assertThrows(ExecutionException.class, () -> {
            parser.parseSongsFromFile(file);
        });
    }
}

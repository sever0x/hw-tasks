package com.sever0x.block1.parser;

import com.sever0x.block1.constants.AttributeConstants;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
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
            Map<String, Integer> statistics = parser.parsePlaylistFromDirectory(testDirectoryPath, AttributeConstants.TITLE);
            assertFalse(statistics.isEmpty());
        });
    }

    @Test
    void testParsePlaylistFromDirectoryWithInvalidPath() {
        assertThrows(IllegalArgumentException.class, () ->
                parser.parsePlaylistFromDirectory("", AttributeConstants.TITLE));
        assertThrows(IllegalArgumentException.class, () ->
                parser.parsePlaylistFromDirectory(null, AttributeConstants.TITLE));
        assertThrows(IllegalArgumentException.class, () ->
                parser.parsePlaylistFromDirectory("invalid/path", AttributeConstants.TITLE));
    }

    @Test
    void testParseSongsFromFile() {
        File file = new File(testDirectoryPath + "/favorites.json");
        assertDoesNotThrow(() -> {
            Map<String, Integer> statistics = parser.parseSongsFromFile(file, AttributeConstants.TITLE, new ConcurrentHashMap<>());
            assertFalse(statistics.isEmpty());
        });
    }

    @Test
    void testParseSongsFromFileWithInvalidFile() {
        File file = new File(testDirectoryPath + "/invalid.json");
        assertThrows(ExecutionException.class, () -> {
            parser.parseSongsFromFile(file, AttributeConstants.TITLE, new ConcurrentHashMap<>());
        });
    }
}
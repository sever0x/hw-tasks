package com.sever0x.block1.parser;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sever0x.block1.constants.AttributeConstants;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;

/**
 * A class for parsing JSON files containing songs.
 */
public class JsonPlaylistParser {
    private final ObjectMapper mapper = new ObjectMapper();
    private final int threadCount;

    /**
     * Constructs a JsonPlaylistParser object with the specified thread count.
     *
     * @param threadCount The number of threads to use for parsing.
     */
    public JsonPlaylistParser(int threadCount) {
        this.threadCount = threadCount;
    }

    /**
     * Parses the songs from JSON files in the specified directory and returns statistics based on the provided attribute.
     *
     * @param path      The path to the directory containing JSON files.
     * @param attribute The attribute based on which statistics are generated (e.g., title, artist, album).
     * @return A map containing statistics based on the specified attribute.
     * @throws IllegalArgumentException If the directory path is null or empty, or if the directory does not exist.
     */
    public Map<String, Integer> parsePlaylistFromDirectory(String path, String attribute) {
        validateDirectoryPath(path);

        Map<String, Integer> statistics = new ConcurrentHashMap<>();
        File directory = new File(path);
        File[] files = directory.listFiles();
        ExecutorService executorService = Executors.newFixedThreadPool(threadCount);
        List<Future<Map<String, Integer>>> futures = new ArrayList<>();

        for (File file : files) {
            futures.add(executorService.submit(() -> parseSongsFromFile(file, attribute, statistics)));
        }

        for (Future<Map<String, Integer>> future : futures) {
            try {
                future.get();
            } catch (ExecutionException | InterruptedException e) {
                System.out.println("Invalid JSON file skipped: " + e.getMessage());
            }
        }

        executorService.shutdown();
        return statistics;
    }

    /**
     * Parses songs from a JSON file and updates statistics based on the provided attribute.
     *
     * @param file       The JSON file to parse.
     * @param attribute  The attribute based on which statistics are generated (e.g., title, artist, album).
     * @param statistics A map containing current statistics.
     * @return The updated statistics map.
     * @throws ExecutionException If an error occurs during parsing.
     */
    public Map<String, Integer> parseSongsFromFile(File file, String attribute, Map<String, Integer> statistics) throws ExecutionException {
        JsonFactory jsonFactory = new JsonFactory();
        try (InputStream inputStream = new FileInputStream(file);
             JsonParser jsonParser = jsonFactory.createParser(inputStream);
        ) {
            if (jsonParser.nextToken() != JsonToken.START_ARRAY) {
                throw new IllegalStateException("Root node is not an array");
            }

            while (jsonParser.nextToken() != JsonToken.END_ARRAY) {
                updateStatistics(jsonParser, attribute, statistics);
            }
        } catch (IOException e) {
            throw new ExecutionException("Invalid JSON file: " + file.getName(), e);
        }
        return statistics;
    }

    private void updateStatistics(JsonParser jsonParser, String attribute, Map<String, Integer> statistics) throws IOException {
        JsonNode rootNode = mapper.readTree(jsonParser);

        switch (attribute) {
            case AttributeConstants.TITLE -> incrementValue(statistics, rootNode.get("title").asText());
            case AttributeConstants.ARTIST -> incrementValue(statistics, rootNode.get("artist").get("name").asText());
            case AttributeConstants.ALBUM -> incrementValue(statistics, rootNode.get("album").asText());
            case AttributeConstants.RELEASE_YEAR -> incrementValue(statistics, rootNode.get("releaseYear").asText());
            case AttributeConstants.GENRES -> {
                String[] genreArray = rootNode.get("genres").asText().split(",");
                for (String genre : genreArray) {
                    incrementValue(statistics, genre.trim());
                }
            }
            case AttributeConstants.DURATION -> incrementValue(statistics, rootNode.get("duration").asText());
            default -> throw new IllegalArgumentException("Invalid attribute: " + attribute);
        }
    }

    private void incrementValue(Map<String, Integer> statistics, String key) {
        statistics.put(key, statistics.getOrDefault(key, 0) + 1);
    }

    private void validateDirectoryPath(String path) {
        if (path == null || path.isEmpty()) {
            throw new IllegalArgumentException("Directory path cannot be null or empty.");
        }

        File directory = new File(path);
        if (!directory.exists()) {
            throw new IllegalArgumentException("Directory " + path + " does not exist.");
        }
    }
}

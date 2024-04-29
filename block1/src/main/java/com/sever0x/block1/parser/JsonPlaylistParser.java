package com.sever0x.block1.parser;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sever0x.block1.model.Song;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

/**
 * A class for parsing JSON files containing songs.
 */
public class JsonPlaylistParser {
    private final ObjectMapper mapper = new ObjectMapper();
    private final int threadCount;

    /**
     * Constructor for the JsonPlaylistParser class.
     *
     * @param threadCount The number of threads for parallel parsing.
     */
    public JsonPlaylistParser(int threadCount) {
        this.threadCount = threadCount;
    }

    /**
     * Parses JSON files containing songs from the specified directory.
     *
     * @param path The path to the directory containing JSON files.
     * @return A list of Song objects parsed from the JSON files.
     */
    public List<Song> parsePlaylistFromDirectory(String path) {
        validateDirectoryPath(path);

        List<Song> songs = new CopyOnWriteArrayList<>();
        File directory = new File(path);
        File[] files = directory.listFiles();
        ExecutorService executorService = Executors.newFixedThreadPool(threadCount);
        List<Future<List<Song>>> futures = new ArrayList<>();

        for (File file : files) {
            futures.add(executorService.submit(() -> parseSongsFromFile(file)));
        }

        for (Future<List<Song>> future : futures) {
            try {
                songs.addAll(future.get());
            } catch (ExecutionException | InterruptedException e) {
                System.out.println("Invalid JSON file skipped: " + e.getMessage());
            }
        }

        executorService.shutdown();
        return songs;
    }

    /**
     * Parses a JSON file containing songs.
     *
     * @param file The JSON file to be parsed.
     * @return A list of Song objects parsed from the JSON file.
     * @throws ExecutionException If the JSON file is invalid.
     */
    public List<Song> parseSongsFromFile(File file) throws ExecutionException {
        List<Song> songs = new ArrayList<>();
        JsonFactory jsonFactory = new JsonFactory();
        try (InputStream inputStream = new FileInputStream(file);
             JsonParser jsonParser = jsonFactory.createParser(inputStream);
        ) {
            if (jsonParser.nextToken() != JsonToken.START_ARRAY) {
                throw new IllegalStateException("Root node is not an array");
            }

            while (jsonParser.nextToken() != JsonToken.END_ARRAY) {
                Song song = mapper.readValue(jsonParser, Song.class);
                songs.add(song);
            }
        } catch (IOException e) {
            throw new ExecutionException("Invalid JSON file: " + file.getName(), e);
        }
        return songs;
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

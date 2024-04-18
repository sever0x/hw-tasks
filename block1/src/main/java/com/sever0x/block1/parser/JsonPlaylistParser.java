package com.sever0x.block1.parser;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sever0x.block1.model.Song;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class JsonPlaylistParser {
    private final ObjectMapper mapper = new ObjectMapper();
    private final int threadCount;

    public JsonPlaylistParser(int threadCount) {
        this.threadCount = threadCount;
    }

    public List<Song> parsePlaylistFromDirectory(String path) {
        validateDirectoryPath(path);

        List<Song> songs = new ArrayList<>();
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

    public List<Song> parseSongsFromFile(File file) throws ExecutionException {
        List<Song> songs = new ArrayList<>();
        try (InputStream inputStream = new FileInputStream(file);
             BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream)) {
            JsonNode rootNode = mapper.readTree(bufferedInputStream);
            if (rootNode.isArray()) {
                for (JsonNode node : rootNode) {
                    Song song = mapper.treeToValue(node, Song.class);
                    songs.add(song);
                }
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

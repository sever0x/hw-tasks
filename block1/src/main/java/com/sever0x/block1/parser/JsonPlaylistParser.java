package com.sever0x.block1.parser;

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
                e.printStackTrace();
            }
        }

        executorService.shutdown();
        return songs;
    }

    private List<Song> parseSongsFromFile(File file) {
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
            e.printStackTrace();
        }

        return songs;
    }
}
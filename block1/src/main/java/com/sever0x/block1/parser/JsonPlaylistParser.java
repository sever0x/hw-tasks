package com.sever0x.block1.parser;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sever0x.block1.model.Song;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class JsonPlaylistParser {

    private static final ObjectMapper mapper = new ObjectMapper();

    public static List<Song> parsePlaylistFromDirectory(String path) {
        List<Song> songs = new ArrayList<>();
        File directory = new File(path);
        File[] files = directory.listFiles();

        ExecutorService executorService = Executors.newFixedThreadPool(4);
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

        return songs;
    }

    private static List<Song> parseSongsFromFile(File file) {
        List<Song> songs = new ArrayList<>();
        try (InputStream inputStream = new FileInputStream(file)) {
            JsonNode rootNode = mapper.readTree(inputStream);
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

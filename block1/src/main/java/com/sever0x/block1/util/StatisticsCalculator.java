package com.sever0x.block1.util;

import com.sever0x.block1.model.Song;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StatisticsCalculator {
    public Map<String, Integer> calculateStatistics(List<Song> songs, String attribute) {
        Map<String, Integer> statistics = new HashMap<>();

        for (Song song : songs) {
            updateStatistics(statistics, song, attribute);
        }

        return statistics;
    }

    private void updateStatistics(Map<String, Integer> statistics, Song song, String attribute) {
        switch (attribute) {
            case "title" -> incrementValue(statistics, song.getTitle());
            case "artist" -> incrementValue(statistics, song.getArtist().getName());
            case "album" -> incrementValue(statistics, song.getAlbum());
            case "releaseYear" -> incrementValue(statistics, String.valueOf(song.getReleaseYear()));
            case "genres" -> {
                for (String genre : song.getGenres()) {
                    incrementValue(statistics, genre);
                }
            }
            case "duration" -> incrementValue(statistics, String.valueOf(song.getDuration()));
        }
    }

    private void incrementValue(Map<String, Integer> statistics, String key) {
        statistics.put(key, statistics.getOrDefault(key, 0) + 1);
    }
}

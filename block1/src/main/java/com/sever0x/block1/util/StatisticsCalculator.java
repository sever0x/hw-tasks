package com.sever0x.block1.util;

import com.sever0x.block1.constants.AttributeConstants;
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
            case AttributeConstants.TITLE -> incrementValue(statistics, song.getTitle());
            case AttributeConstants.ARTIST -> incrementValue(statistics, song.getArtist().getName());
            case AttributeConstants.ALBUM -> incrementValue(statistics, song.getAlbum());
            case AttributeConstants.RELEASE_YEAR -> incrementValue(statistics, String.valueOf(song.getReleaseYear()));
            case AttributeConstants.GENRES -> {
                String[] genreArray = song.getGenres().split(",");
                for (String genre : genreArray) {
                    incrementValue(statistics, genre.trim());
                }
            }
            case AttributeConstants.DURATION -> incrementValue(statistics, String.valueOf(song.getDuration()));
            default -> throw new IllegalArgumentException("Invalid attribute: " + attribute);
        }
    }

    private void incrementValue(Map<String, Integer> statistics, String key) {
        statistics.put(key, statistics.getOrDefault(key, 0) + 1);
    }
}

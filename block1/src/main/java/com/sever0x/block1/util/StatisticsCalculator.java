package com.sever0x.block1.util;

import com.sever0x.block1.model.Song;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StatisticsCalculator {

    public static Map<String, Integer> calculateStatistics(List<Song> songs, String attribute) {
        Map<String, Integer> statistics = new HashMap<>();

        for (Song song : songs) {
            switch (attribute) {
                case "title" -> {
                    String title = song.getTitle();
                    statistics.put(title, statistics.getOrDefault(title, 0) + 1);
                }
                case "artist" -> {
                    String artistName = song.getArtist().getName();
                    statistics.put(artistName, statistics.getOrDefault(artistName, 0) + 1);
                }
                case "album" -> {
                    String album = song.getAlbum();
                    statistics.put(album, statistics.getOrDefault(album, 0) + 1);
                }
                case "releaseYear" -> {
                    String releaseYear = String.valueOf(song.getReleaseYear());
                    statistics.put(releaseYear, statistics.getOrDefault(releaseYear, 0) + 1);
                }
                case "genres" -> {
                    for (String genre : song.getGenres()) {
                        statistics.put(genre, statistics.getOrDefault(genre, 0) + 1);
                    }
                }
                case "duration" -> {
                    String duration = String.valueOf(song.getDuration());
                    statistics.put(duration, statistics.getOrDefault(duration, 0) + 1);
                }
            }
        }

        return statistics;
    }
}

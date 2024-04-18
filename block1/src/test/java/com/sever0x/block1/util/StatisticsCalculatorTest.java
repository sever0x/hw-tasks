package com.sever0x.block1.util;

import com.sever0x.block1.model.Artist;
import com.sever0x.block1.model.Song;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

class StatisticsCalculatorTest {
    private StatisticsCalculator calculator;
    private List<Song> songs;

    @BeforeEach
    void setup() {
        calculator = new StatisticsCalculator();
        songs = List.of(
                new Song("Title1", new Artist("Artist1", "Country1"), "Album1", List.of("Genre1"), 300, 2000),
                new Song("Title2", new Artist("Artist2", "Country2"), "Album2", List.of("Genre2"), 200, 2001),
                new Song("Title1", new Artist("Artist1", "Country1"), "Album1", List.of("Genre1"), 300, 2000)
        );
    }

    @Test
    void testCalculateStatistics() {
        Map<String, Integer> statistics = calculator.calculateStatistics(songs, "title");
        assertEquals(2, statistics.get("Title1"));
        assertEquals(1, statistics.get("Title2"));

        statistics = calculator.calculateStatistics(songs, "artist");
        assertEquals(2, statistics.get("Artist1"));
        assertEquals(1, statistics.get("Artist2"));

        statistics = calculator.calculateStatistics(songs, "album");
        assertEquals(2, statistics.get("Album1"));
        assertEquals(1, statistics.get("Album2"));

        statistics = calculator.calculateStatistics(songs, "releaseYear");
        assertEquals(2, statistics.get("2000"));
        assertEquals(1, statistics.get("2001"));

        statistics = calculator.calculateStatistics(songs, "genres");
        assertEquals(2, statistics.get("Genre1"));
        assertEquals(1, statistics.get("Genre2"));

        statistics = calculator.calculateStatistics(songs, "duration");
        assertEquals(2, statistics.get("300"));
        assertEquals(1, statistics.get("200"));
    }

    @Test
    void testCalculateStatisticsWithEmptyList() {
        Map<String, Integer> statistics = calculator.calculateStatistics(new ArrayList<>(), "title");
        assertTrue(statistics.isEmpty());
    }

    @Test
    void testCalculateStatisticsWithInvalidAttribute() {
        assertThrows(IllegalArgumentException.class, () -> {
            calculator.calculateStatistics(songs, "invalid");
        });
    }
}


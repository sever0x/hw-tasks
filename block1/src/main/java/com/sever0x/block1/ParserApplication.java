package com.sever0x.block1;

import com.sever0x.block1.io.XmlWriter;
import com.sever0x.block1.model.Song;
import com.sever0x.block1.parser.JsonPlaylistParser;
import com.sever0x.block1.util.StatisticsCalculator;

import java.util.List;
import java.util.Map;

public class ParserApplication {
    public static void main(String[] args) {
        if (args.length != 2) {
            System.out.println("Usage: java -jar music-stats.jar <directory_path> <attribute_name>");
            return;
        }

        String directoryPath = args[0];
        String attributeName = args[1];

        JsonPlaylistParser parser1 = new JsonPlaylistParser(1);
        JsonPlaylistParser parser2 = new JsonPlaylistParser(2);
        JsonPlaylistParser parser4 = new JsonPlaylistParser(4);
        JsonPlaylistParser parser8 = new JsonPlaylistParser(8);

        long start, end;

        start = System.currentTimeMillis();
        List<Song> songs1 = parser1.parsePlaylistFromDirectory(directoryPath);
        end = System.currentTimeMillis();
        System.out.println("Execution time with 1 thread: " + (end - start) + "ms");

        start = System.currentTimeMillis();
        List<Song> songs2 = parser2.parsePlaylistFromDirectory(directoryPath);
        end = System.currentTimeMillis();
        System.out.println("Execution time with 2 threads: " + (end - start) + "ms");

        start = System.currentTimeMillis();
        List<Song> songs4 = parser4.parsePlaylistFromDirectory(directoryPath);
        end = System.currentTimeMillis();
        System.out.println("Execution time with 4 threads: " + (end - start) + "ms");

        start = System.currentTimeMillis();
        List<Song> songs8 = parser8.parsePlaylistFromDirectory(directoryPath);
        end = System.currentTimeMillis();
        System.out.println("Execution time with 8 threads: " + (end - start) + "ms");

        StatisticsCalculator calculator = new StatisticsCalculator();
        Map<String, Integer> statistics = calculator.calculateStatistics(songs1, attributeName);
        XmlWriter writer = new XmlWriter();
        writer.writeToXml(statistics, attributeName);
        System.out.println("Statistics written to statistics_by_" + attributeName + ".xml");
    }
}

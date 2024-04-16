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

        List<Song> songs = JsonPlaylistParser.parsePlaylistFromDirectory(directoryPath);
        Map<String, Integer> statistics = StatisticsCalculator.calculateStatistics(songs, attributeName);
        XmlWriter.writeToXml(statistics, attributeName);
        System.out.println("Statistics written to statistics_by_" + attributeName + ".xml");
    }
}

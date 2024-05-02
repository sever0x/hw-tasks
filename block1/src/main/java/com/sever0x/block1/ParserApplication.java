package com.sever0x.block1;

import com.sever0x.block1.io.XmlWriter;
import com.sever0x.block1.parser.JsonPlaylistParser;

import java.util.Map;

public class ParserApplication {
    public static void main(String[] args) {
        if (args.length != 3) {
            System.out.println("Usage: java -jar target/block1-1.0-SNAPSHOT.jar <directory_path> <attribute_name> <thread_count>");
            return;
        }

        String directoryPath = args[0];
        String attributeName = args[1];
        int threadCount = Integer.parseInt(args[2]);

        JsonPlaylistParser parser = new JsonPlaylistParser(threadCount);
        long start, end;

        start = System.currentTimeMillis();
        Map<String, Integer> statistics = parser.parsePlaylistFromDirectory(directoryPath, attributeName);
        end = System.currentTimeMillis();

        System.out.println("Execution time with " + threadCount + " thread(s): " + (end - start) + "ms");

        XmlWriter writer = new XmlWriter();
        writer.writeToXml(statistics, attributeName);
        System.out.println("Statistics written to statistics_by_" + attributeName + ".xml");
    }
}

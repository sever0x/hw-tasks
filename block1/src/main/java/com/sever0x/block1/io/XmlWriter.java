package com.sever0x.block1.io;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;

/**
 * A class for writing statistics to an XML file.
 */
public class XmlWriter {

    /**
     * Writes statistics to an XML file.
     *
     * @param statistics A map containing statistics in the format "value" -> "count".
     * @param attribute  The attribute for which the statistics were collected.
     */
    public void writeToXml(Map<String, Integer> statistics, String attribute) {
        try (FileWriter writer = new FileWriter("statistics_by_" + attribute + ".xml")) {
            writer.write("<statistics>\n");

            statistics.entrySet().stream()
                    .sorted((entry1, entry2) -> entry2.getValue().compareTo(entry1.getValue()))
                    .forEach(entry -> {
                        try {
                            writer.write("  <item>\n");
                            writer.write("    <value>" + entry.getKey() + "</value>\n");
                            writer.write("    <count>" + entry.getValue() + "</count>\n");
                            writer.write("  </item>\n");
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    });
            writer.write("</statistics>");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

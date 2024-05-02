package com.sever0x.block2.parser.json;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sever0x.block2.validation.ValidationUtils;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

@Component
public class JsonPlaylistParser {
    private final ObjectMapper mapper = new ObjectMapper();

    public List<ParsedSong> parseSongsFromFile(InputStream inputStream) throws IOException {
        List<ParsedSong> parsedSongs = new ArrayList<>();
        JsonFactory jsonFactory = new JsonFactory();

        try (JsonParser jsonParser = jsonFactory.createParser(inputStream)) {
            if (jsonParser.nextToken() != JsonToken.START_ARRAY) {
                throw new IllegalStateException("Root node is not an array");
            }

            while (jsonParser.nextToken() != JsonToken.END_ARRAY) {
                try {
                    JsonNode rootNode = mapper.readTree(jsonParser);
                    parsedSongs.add(new ParsedSong(rootNode));
                } catch (ValidationUtils.ValidationException e) {
                    System.out.println("Skipping invalid record: " + e.getMessage());
                }
            }
        }

        return parsedSongs;
    }
}
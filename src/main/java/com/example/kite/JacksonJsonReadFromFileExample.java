package com.example.kite;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.MappingIterator;
import com.fasterxml.jackson.databind.json.JsonMapper;

import java.io.FileInputStream;
import java.io.IOException;

public class JacksonJsonReadFromFileExample {

    public static void main(String[] args)
    {
        System.out.println("Reading JSON from a file");
        System.out.println("----------------------------");

        String path = "/Users/asingh/Google Drive/mymooc-workspace/jsonExample/src/main/resources/test-data-multi-line.json";
        try (FileInputStream fis = new FileInputStream(path)) {

            final JsonMapper mapper = new JsonMapper();
            try (MappingIterator<JsonNode> it = mapper.readerFor(JsonNode.class)
                    .readValues(fis)) {
                while (it.hasNextValue()) {
                    JsonNode node = it.nextValue();
                    System.out.println(node.toString());
                }
            }

        }
        catch (IOException e) {
            e.printStackTrace();
        }

    }
}
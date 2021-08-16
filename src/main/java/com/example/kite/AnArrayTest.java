package com.example.kite;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.MappingIterator;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class AnArrayTest {

    public static void main(String[] args)
    {
        System.out.println("Reading JSON as Array from a file");
        System.out.println("----------------------------");

        String path = "/Users/asingh/repo/json-conversion/src/main/resources/test-data-multi-line-array.json";
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

class MyPojo {
    private List<String> cars = new ArrayList<String>();
    private String name;
    private String job;
    private String city;
}
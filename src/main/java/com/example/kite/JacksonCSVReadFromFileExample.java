package com.example.kite;

import com.fasterxml.jackson.databind.MappingIterator;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvParser;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;
import java.util.Map;

public class JacksonCSVReadFromFileExample {

    public static void main(String[] args)
    {
        System.out.println("Reading CSV from a file");
        System.out.println("----------------------------");

        String path = "/Users/asingh/Google Drive/mymooc-workspace/jsonExample/src/main/resources/test-data.csv";
        try (FileInputStream fis = new FileInputStream(path);
             FileInputStream hFis = new FileInputStream(path);) {

            final CsvMapper mapper = new CsvMapper();

            try (MappingIterator<List<String>> it = mapper.readerForListOf(String.class)
                    .with(CsvParser.Feature.WRAP_AS_ARRAY)
                    .readValues(fis)) {

                while (it.hasNextValue()) {
                    List<String> node = it.nextValue();
                    System.out.println(node.toString());
                }
            }


            CsvSchema headerSchema = CsvSchema.emptySchema().withHeader();
            try (MappingIterator<Map<String, String>> it = mapper.readerForMapOf(String.class)
                    .with(headerSchema)
                    .readValues(hFis)) {

                while (it.hasNextValue()) {
                    Map<String, String> node = it.nextValue();
                    System.out.println(node.toString());
                }

            }

        }
        catch (IOException e) {
            e.printStackTrace();
        }

    }
}
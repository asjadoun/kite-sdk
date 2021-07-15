package com.example.kite;

import org.apache.avro.generic.GenericData;
import org.apache.avro.generic.GenericRecord;
import org.kitesdk.data.*;

public class KiteReaderTest {
    public void writeUserToView(View<GenericRecord> dataset, GenericRecord record) {
        DatasetWriter<GenericRecord> writer = null;
        try {
            writer = dataset.newWriter();
            writer.write(record);
        } finally {
            if (writer != null) {
                writer.close();
            }
        }
    }

    void testData(){
        Dataset<GenericData.Record> products = Datasets.load ("dataset:hive:demo/sample_07" ,GenericData.Record.class);
        DatasetReader<GenericData.Record> reader = null;

        View<GenericData.Record> ratings = Datasets.load("view:hive:demo/sample_07?product=apple" ,GenericData.Record.class);

        try {
            reader = ratings.newReader();
            for (GenericRecord product : reader) {
                System.out.println(product);
            }
        } finally {
            if (reader != null) {
                reader.close();
            }
        }
    }

    public static void main(String[] args) {
        KiteReaderTest kr = new KiteReaderTest();
        kr.testData();
    }
}
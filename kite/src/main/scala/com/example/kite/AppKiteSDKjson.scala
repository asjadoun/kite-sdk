package com.example.kite

import better.files.{File, Resource}
import com.example.kite.KiteSDKcommon.{prepareDataset, readDataset, writeToDataset}
import com.fasterxml.jackson.databind.JsonNode
import org.apache.avro.Schema
import org.apache.avro.generic.{GenericRecord, GenericRecordBuilder}
import org.kitesdk.data._
import org.kitesdk.data.spi.JsonUtil
import org.kitesdk.data.spi.filesystem.{CSVProperties, CSVUtil}

import java.io.ByteArrayInputStream

object AppKiteSDKjson {

  def main(args: Array[String]): Unit = {

    val recordCount = 5
    val format = Formats.AVRO
//    val format = Formats.PARQUET

    val dataFile = "test-data-multi-line-array.json"
    val json = JsonUtil.parse(Resource.getAsStream(dataFile))
    // this method take first record to evaluate datatype
    val schema1: Schema = JsonUtil.inferSchema(json, s"${format.getName}.schema")
    // this method take <n> records to evaluate datatype and unionize them
    val schema: Schema = JsonUtil.inferSchema(Resource.getAsStream(dataFile), "my.avro.schema", 3)

    val partitionStrategy: PartitionStrategy = new PartitionStrategy.Builder()
      .identity("myFieldA")
      .build()

    val descriptor: DatasetDescriptor = new DatasetDescriptor.Builder()
      .schema(schema)
//      .partitionStrategy(partitionStrategy)
      .format(format)
      .build()

    val uri = s"dataset:file:./build/storage/mynamespace/mydata/${format.getName}?absolute=false"
    val exists: Boolean = Datasets.exists(uri)

    val partitionedData: Dataset[GenericRecord] = {
      if (exists) Datasets.load(uri)
      else Datasets.create(uri, descriptor, classOf[GenericRecord])
    }

    val records: List[GenericRecord] = prepareDataset(recordCount, partitionedData.getDescriptor.getSchema)

    writeToDataset[GenericRecord](records, partitionedData)

    val res: List[GenericRecord] = readDataset[GenericRecord](partitionedData.getDataset)
    res.foreach(println)
  }
}

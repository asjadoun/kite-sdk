package com.example.kite

import better.files.Resource
import com.fasterxml.jackson.databind.JsonNode
import org.apache.avro.Schema
import org.apache.avro.generic.{GenericRecord, GenericRecordBuilder}
import org.kitesdk.data._
import org.kitesdk.data.spi.JsonUtil

object AppReadJsonKiteSDK {

  def prepareDataset(count: Int, schema: Schema): List[GenericRecord] = {
    def prepare(idx: Int): GenericRecord = {
      val builder = new GenericRecordBuilder(schema)
      builder.set("myFieldA", 100 + idx)
      builder.set("myFieldB", s"anil#$idx")
      builder.build()
    }
    (1 until count).map(prepare).to(List)
  }

  def writeToDataset[E](records: List[E], dataset: Dataset[E]) = {
    val writer: DatasetWriter[E] = dataset.newWriter()
    try{
      records.foreach(writer.write)
    } finally {
      writer.close()
    }
  }

  def readDataset[E](dataset: Dataset[E]): List[E] = {
    val reader: DatasetReader[E] = dataset.newReader()
    try {
      import scala.jdk.CollectionConverters._
      reader.iterator().asScala.to(List)
    } finally {
      reader.close()
    }
  }

  def main(args: Array[String]): Unit = {

//    val parser = new Schema.Parser()
//    val schema: Schema = parser.parse(Resource.getAsStream("user-schema.avsc"))

    val recordCount = 5
//    val format = Formats.AVRO
    val format = Formats.PARQUET

    val json: JsonNode = JsonUtil.parse(Resource.getAsStream("test-data-multi-line.json"))
    val inferSchema: Schema = JsonUtil.inferSchema(json, s"${format.getName}.schema")

    val partitionStrategy: PartitionStrategy = new PartitionStrategy.Builder()
      .identity("myFieldA")
      .build()

    val descriptor: DatasetDescriptor = new DatasetDescriptor.Builder()
      .schema(inferSchema)
      .partitionStrategy(partitionStrategy)
      .format(format)
      .build()

    val uri = s"dataset:file:./build/storage/mynamespace/mydata/${format.getName}?absolute=false"
    val exists: Boolean = Datasets.exists(uri)

    val partitionedData: Dataset[GenericRecord] = {
      if (exists) Datasets.load(uri)
      else Datasets.create(uri, descriptor, classOf[GenericRecord])
    }

    val records = prepareDataset(recordCount, partitionedData.getDescriptor.getSchema)

    writeToDataset[GenericRecord](records, partitionedData)

    val res = readDataset[GenericRecord](partitionedData.getDataset)
    res.foreach(println)
  }
}

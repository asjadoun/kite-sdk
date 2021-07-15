package com.example.kite

import better.files.Resource
import com.fasterxml.jackson.databind.JsonNode
import org.apache.avro.Schema
import org.apache.avro.generic.{GenericRecord, GenericRecordBuilder}
import org.kitesdk.data._
import org.kitesdk.data.spi.JsonUtil

import java.util

object AppReadJsonKiteSDK {

  def main(args: Array[String]): Unit = {
    //    System.setProperty("hadoop.home.dir", "/")

    val json: JsonNode = JsonUtil.parse(Resource.getAsStream("single.json"))
    val inferSchema: Schema = JsonUtil.inferSchema(json, "mySchema")

    val userSchema: String = """{
                               |  "name": "MyClass",
                               |  "type": "record",
                               |  "namespace": "com.savant.avro",
                               |  "fields": [
                               |    {
                               |      "name": "myFieldA",
                               |      "type": "int"
                               |    },
                               |    {
                               |      "name": "myFieldB",
                               |      "type": "string"
                               |    }
                               |  ]
                               |}""".stripMargin

    val parser = new Schema.Parser()
    val schema: Schema = parser.parse(userSchema)

    val partitionStrategy: PartitionStrategy = new PartitionStrategy.Builder()
      .identity("myFieldA")
      .build()

    val descriptor: DatasetDescriptor = new DatasetDescriptor.Builder()
      .schema(inferSchema)
      .partitionStrategy(partitionStrategy)
      //          .format(Formats.PARQUET)
      .format(Formats.AVRO)
      .build()

//    val uri = "dataset:file://C:/Users/Anil/storage/mynamespace/mydata/parquest?absolute=true"
    val uri = "dataset:file://C:/Users/Anil/storage/mynamespace/mydata/avro?absolute=true"
//   val uri = "dataset:file:/Users/asingh/storage/mynamespace/mydata/parquest?absolute=true"
//    val uri = "dataset:file:/Users/asingh/storage/mynamespace/mydata/avro?absolute=true"

    val exists: Boolean = Datasets.exists(uri)

    val partitionedData: Dataset[GenericRecord] = {
      if (exists) Datasets.load(uri)
      else Datasets.create(uri, descriptor, classOf[GenericRecord])
    }

    (1 until 6).foreach { idx =>
      val builder = new GenericRecordBuilder(partitionedData.getDescriptor.getSchema)
      builder.set("myFieldA", 100 + idx)
      builder.set("myFieldB", s"anil#$idx")

      val writer: DatasetWriter[GenericRecord] = partitionedData.newWriter()
      writer.write(builder.build())
      writer.close()
    }

    val reader: DatasetReader[GenericRecord] = partitionedData.newReader()
    import scala.jdk.CollectionConverters._
    val list: util.Iterator[GenericRecord] = reader.iterator()
    list.asScala.foreach(println)
    reader.close()

    println(descriptor.getSchema)
  }
}

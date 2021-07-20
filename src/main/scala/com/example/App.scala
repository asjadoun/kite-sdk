package com.example

import better.files.{Resource, using}
import com.example.jack.AppReadJsonJackson.streamJson
import com.example.serialize.serialization.fromJsonToGenericRecord
import com.fasterxml.jackson.databind.JsonNode
import org.apache.avro.Schema
import org.kitesdk.data.spi.JsonUtil

import java.io.FileInputStream

object App {

  val dataFile = "test-data.json"
  val path = Resource.getUrl(dataFile).getPath
  val inputStream = Resource.getAsStream(dataFile)
  val fileInputStream = new FileInputStream(path)

  def main(args: Array[String]): Unit = {
    using(inputStream) {
      incoming =>
        val schema: Schema = JsonUtil.inferSchema(incoming, "my.avro.schema", 2)
        using(fileInputStream) {
          fis =>
            streamJson(fis)
              .map(json => fromJsonToGenericRecord(json.toString, schema))
              .foreach(println)
        }
    }

  }

}

package com.example

import better.files.{Resource, using}
import com.example.jack.AppReadJsonJackson.readJsonFile
import com.example.serialize.serialization.{fromJsonToByteBuffer, fromJsonToByteBuffer2}
import org.apache.avro.Schema
import org.kitesdk.data.spi.JsonUtil

import java.io.{FileInputStream, InputStream}
import java.nio.charset.StandardCharsets

object App {

  val dataFile = "test-data.json"
  val path: String = Resource.getUrl(dataFile).getPath
  val inputStream: InputStream = Resource.getAsStream(dataFile)
  val fileInputStream = new FileInputStream(path)

  def main(args: Array[String]): Unit = {
    using(inputStream) {
      incoming =>
        val schema: Schema = JsonUtil.inferSchema(incoming, "my.avro.schema", 2)
        using(fileInputStream) {
          fis =>
            readJsonFile(fis)
              .map(json => fromJsonToByteBuffer2(json.toString, schema))
              .map(x => new String(x.array(), StandardCharsets.UTF_8))
              .foreach(println)
        }
    }

  }

}

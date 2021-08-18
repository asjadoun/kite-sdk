package com.example

import better.files.{Resource, using}
import com.example.jack.AppReadJsonJackson.streamJsonAsScala
import com.example.kite.utils.JsonUtil
import com.example.serialize.serialization.{fromJsonToByteBuffer, fromJsonToGenericRecord}
import org.apache.avro.Schema

import java.io.{FileInputStream, InputStream}
import java.nio.charset.StandardCharsets

object App {

//  val dataFile = "test-data.json"
  val dataFile = "test-data-multi-line.json"
//  val dataFile = "test-data-multi-line-array.json"

  val path: String = Resource.getUrl(dataFile).getPath
  val inputStream: InputStream = Resource.getAsStream(dataFile)
  val fileInputStream = new FileInputStream(path)

  def main(args: Array[String]): Unit = {
    using(inputStream) {
      incoming =>
        val schema: Schema = JsonUtil.inferSchema(incoming, "my.avro.schema", 4)
        println(s"${schema.toString(true)}")

        using(fileInputStream) {
          fis =>
            streamJsonAsScala(fis)
              .map{json => println(s"Processing: $json"); json}
              .map{json => fromJsonToGenericRecord(json.toString, schema)}
              .map{json => println(s"Generic: $json"); (json, fromJsonToByteBuffer(json.toString, schema))}
              .map{
                case (rec, byte) =>
                  (s"Byte Length: ${byte.limit()}  Byte Value: ", new String(byte.array(), StandardCharsets.UTF_8), s"GenericRecord: ${rec.toString}")
              }
              .foreach(println)
        }
    }

  }

}

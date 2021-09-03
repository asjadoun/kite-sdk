package com.example

import better.files.{File, Resource, using}
import com.example.jack.AppReadJsonJackson.streamJsonAsScala
import com.example.kite.utils.JsonUtil
import com.example.serialize.serialization.{fromJsonToByteBuffer, fromJsonToGenericRecord}
import com.example.write.AvroWriter.toAvro
import org.apache.avro.Schema

import java.io.{FileInputStream, InputStream}
import java.nio.ByteBuffer
import java.nio.charset.StandardCharsets

object App {

//  val dataFile = "test-data.json"
//  val dataFile = "test-data-multi-line.json"
//  val dataFile = "test-data-multi-line-array.json"
  val dataFile = "one-attribute-with-max-values.json"

  val path: String = Resource.getUrl(dataFile).getPath
  val inputStream: InputStream = Resource.getAsStream(dataFile)
  val fileInputStream = new FileInputStream(path)

  val extSchema = Resource.getAsString("one-attribute-with-max-values.avsc")
  def main(args: Array[String]): Unit = {
    using(inputStream) {
      incoming =>
        val schema: Schema = new Schema.Parser().parse(extSchema)
//        val schema: Schema = JsonUtil.inferSchema(incoming, "my.avro.schema", 9)
        println(s"${schema.toString(true)}")

        using(fileInputStream) {
          fis =>
            val records: Iterator[ByteBuffer] = {
              streamJsonAsScala(fis)
              .map{json => println(s"Processing: $json"); (json, schema)}
              .map{case(json, rSchema) => (fromJsonToGenericRecord(json.toString, rSchema), rSchema)}
              .map{case(json, rSchema) => println(s"Generic: $json"); (json, fromJsonToByteBuffer(json.toString, rSchema))}
              .map{
                case (json, bytes) =>
                  println(s"Byte Length: ${bytes.limit()}  Byte Value: ", new String(bytes.array(), StandardCharsets.UTF_8), s"GenericRecord: ${json.toString}")
                  bytes
              }
            }

            toAvro(File(s"./out/avro/$dataFile.avro"), records.toList, schema)
        }
    }

  }

}

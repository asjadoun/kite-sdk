package com.example.jack

import better.files.Resource
import com.fasterxml.jackson.core.{JsonFactory, JsonParser}
import com.fasterxml.jackson.databind.json.JsonMapper
import com.fasterxml.jackson.databind.{JsonNode, MappingIterator}
import com.fasterxml.jackson.module.scala.DefaultScalaModule

import java.io.{FileInputStream, IOException}
import java.util
import scala.jdk.CollectionConverters._

object AppReadJsonAsMap {

  def streamJson(inputStream: FileInputStream): LazyList[util.Map[String, String]] = {
    val mapper = new JsonMapper.Builder(new JsonMapper)
      .configure(JsonParser.Feature.ALLOW_COMMENTS, true)
      .configure(JsonParser.Feature.ALLOW_SINGLE_QUOTES, true)
      .configure(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES, true)
      .build()

    val it: MappingIterator[util.Map[String, String]] =
      mapper
      .readerFor(classOf[util.Map[String, String]])
      .readValues(inputStream)
    it.asScala.to(LazyList)
  }


  def main(args: Array[String]): Unit = {
    println("Reading JSON from a file")
    println("----------------------------")
    val dataFile = "test-data-complex.json"
//    val dataFile = "test-data.json"
//    val dataFile = "test-data-multi-line.json"
//    val dataFile = "test-data-multi-line-array.json"

    val path: String = Resource.getUrl(dataFile).getPath
    val fis = new FileInputStream(path)
    try {
//      readJsonFile(fis)
      streamJson(fis).map(_.asScala)
        .map(x => x.toList)
        .foreach{
          x =>
            println(x)
        }
    } catch {
          case e: IOException =>
            e.printStackTrace()
        } finally if (fis != null) fis.close()
  }
}
package com.example.jack

import better.files.Resource
import com.fasterxml.jackson.core.{JsonFactory, JsonParser}
import com.fasterxml.jackson.databind.json.JsonMapper
import com.fasterxml.jackson.databind.{JsonNode, MappingIterator}

import java.io.{FileInputStream, IOException}
import java.util

object AppReadJsonJackson {
  def streamJson(inputStream: FileInputStream): Seq[JsonNode] = {
    val mapper = new JsonMapper()
    val factory: JsonFactory = mapper.getFactory
    val parser: JsonParser = factory.createParser(inputStream)

    val list: util.Iterator[JsonNode] = parser.readValuesAs[JsonNode](classOf[JsonNode])
    import scala.jdk.CollectionConverters._
    list.asScala.to(LazyList)
  }

  def readJsonFile(inputStream: FileInputStream): LazyList[JsonNode] =
  {
    val mapper = new JsonMapper
    val it: MappingIterator[JsonNode] = mapper.readerFor(classOf[JsonNode]).readValues(inputStream)
    val nodes: LazyList[JsonNode] = try {
      import scala.jdk.CollectionConverters._
      it.readAll().asScala.to(LazyList)
    }

      //        try while (it.hasNextValue) {
      //          val node: JsonNode = it.nextValue
      //          node.asText()
      //        }
    finally if (it != null) it.close()
    nodes
  }
  def main(args: Array[String]): Unit = {
    println("Reading JSON from a file")
    println("----------------------------")
    val dataFile = "test-data.json"
    val path: String = Resource.getUrl(dataFile).getPath
    val fis = new FileInputStream(path)
    try {
//      readJsonFile(fis)
      streamJson(fis)
        .foreach(println)
    } catch {
          case e: IOException =>
            e.printStackTrace()
        } finally if (fis != null) fis.close()
  }
}
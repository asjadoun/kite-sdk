package com.example.jack

import better.files.Resource
import com.fasterxml.jackson.core.{JsonFactory, JsonParser}
import com.fasterxml.jackson.databind.{JsonNode, MappingIterator}
import com.fasterxml.jackson.databind.json.JsonMapper
import com.fasterxml.jackson.dataformat.xml.{XmlFactory, XmlMapper}
import com.fasterxml.jackson.module.scala.DefaultScalaModule

import java.io.{FileInputStream, IOException}
import java.util
import scala.jdk.CollectionConverters._

object AppReadXMLJackson {
  def streamJsonAsScala(inputStream: FileInputStream): Iterator[JsonNode] = {
    val mapper: JsonMapper = JsonMapper.builder().addModule(DefaultScalaModule).build()
    val factory: JsonFactory = mapper.getFactory
    val parser: JsonParser = factory.createParser(inputStream)
    val list: util.Iterator[JsonNode] = parser.readValuesAs[JsonNode](classOf[JsonNode])
    list.asScala
  }
  def streamXml(inputStream: FileInputStream): Seq[String] = {
    val mapper = new XmlMapper()
    val node = mapper.readTree(inputStream)
    val list1 = node.iterator()
    val list2 = node.elements()
    list1.asScala.map(x => x.toString).toSeq
  }

  def readJsonFile(inputStream: FileInputStream): LazyList[JsonNode] =
  {
    val mapper = new JsonMapper
    val it: MappingIterator[JsonNode] = mapper.readerFor(classOf[JsonNode]).readValues(inputStream)
    val nodes: LazyList[JsonNode] = try {
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
    println("Reading XML from a file")
    println("----------------------------")
    val dataFile = "test-data.xml"
    val path: String = Resource.getUrl(dataFile).getPath
    val fis = new FileInputStream(path)
    try {
      //      readJsonFile(fis)
      streamXml(fis)
        .foreach(println)
    } catch {
      case e: IOException =>
        e.printStackTrace()
    } finally if (fis != null) fis.close()
  }
}
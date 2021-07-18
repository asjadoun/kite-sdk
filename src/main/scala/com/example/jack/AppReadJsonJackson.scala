package com.example.jack

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.MappingIterator
import com.fasterxml.jackson.databind.json.JsonMapper

import java.io.FileInputStream
import java.io.IOException
import java.util

object AppReadJsonJackson {
  def streamJson(inputStream: FileInputStream): Stream[JsonNode] =
  {
    val mapper = new JsonMapper
    val it: MappingIterator[JsonNode] = mapper.readerFor(classOf[JsonNode]).readValues(inputStream)
    val nodes: Stream[JsonNode] = try {
      import scala.jdk.CollectionConverters._
      it.readAll().asScala.toStream
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
    val path = "/Users/asingh/Google Drive/mymooc-workspace/jsonExample/src/main/resources/test-data-multi-line.json"
    val fis = new FileInputStream(path)
    try {
      streamJson(fis)
        .foreach(println)
    } catch {
          case e: IOException =>
            e.printStackTrace()
        } finally if (fis != null) fis.close()
  }
}
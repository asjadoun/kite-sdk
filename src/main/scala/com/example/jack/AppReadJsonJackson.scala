package com.example.jack

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.MappingIterator
import com.fasterxml.jackson.databind.json.JsonMapper
import java.io.FileInputStream
import java.io.IOException

object AppReadJsonJackson {
  def main(args: Array[String]): Unit = {
    println("Reading JSON from a file")
    println("----------------------------")
    val path = "/Users/asingh/Google Drive/mymooc-workspace/jsonExample/src/main/resources/test-data-multi-line.json"
    try {
      val fis = new FileInputStream(path)
      try {
        val mapper = new JsonMapper
        try {
          val it: MappingIterator[JsonNode] = mapper.readerFor(classOf[JsonNode]).readValues(fis)
          try while ( {
            it.hasNextValue
          }) {
            val node: JsonNode = it.nextValue
            println(node.toString)
          }
          finally if (it != null) it.close()
        }
      } catch {
        case e: IOException =>
          e.printStackTrace()
      } finally if (fis != null) fis.close()
    }
  }
}
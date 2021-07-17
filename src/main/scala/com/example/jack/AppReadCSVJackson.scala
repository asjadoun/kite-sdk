package com.example.jack

import com.fasterxml.jackson.databind.MappingIterator
import com.fasterxml.jackson.dataformat.csv.{CsvMapper, CsvParser, CsvSchema}

import java.io.{FileInputStream, IOException}
import java.util


object AppReadCSVJackson {
  def main(args: Array[String]): Unit = {
    println("Reading CSV from a file")
    println("----------------------------")

    val path = "/Users/asingh/Google Drive/mymooc-workspace/jsonExample/src/main/resources/test-data.csv"
    try {
      val fis = new FileInputStream(path)
      val hFis = new FileInputStream(path)
      try {
        val mapper = new CsvMapper
        try {
          val it: MappingIterator[util.List[String]] = mapper.readerForListOf(classOf[String])
            .`with`(CsvParser.Feature.WRAP_AS_ARRAY)
            .readValues(fis)
          try while (it.hasNextValue) {
            val node: util.List[String] = it.nextValue
            println(node.toString)
          }
          finally if (it != null) it.close()
        }

        val headerSchema: CsvSchema = CsvSchema.emptySchema.withHeader
        try {
          val it: MappingIterator[util.Map[String, String]] = mapper.readerForMapOf(classOf[String])
            .`with`(headerSchema)
            .readValues(hFis)
          try while (it.hasNextValue) {
            val node: util.Map[String, String] = it.nextValue
            println(node.toString)
          }
          finally if (it != null) it.close()
        }
      } catch {
        case e: IOException =>
          e.printStackTrace()
      } finally {
        if (fis != null) fis.close()
        if (hFis != null) hFis.close()
      }
    }
  }
}
package com.example.kite

import org.apache.avro.Schema
import org.apache.avro.generic.{GenericRecord, GenericRecordBuilder}
import org.kitesdk.data._

object KiteSDKcommon {

  def prepareDataset(count: Int, schema: Schema): List[GenericRecord] = {
    def prepare(idx: Int): GenericRecord = {
      val builder = new GenericRecordBuilder(schema)
      builder.set("myFieldA", 100 + idx)
      builder.set("myFieldB", s"anil#$idx")
      builder.build()
    }
    (1 until count).map(prepare).to(List)
  }

  def writeToDataset[E](records: List[E], dataset: Dataset[E]): Unit = {
    val writer: DatasetWriter[E] = dataset.newWriter()
    try{
      records.foreach(writer.write)
    } finally {
      writer.close()
    }
  }

  def readDataset[E](dataset: Dataset[E]): List[E] = {
    val reader: DatasetReader[E] = dataset.newReader()
    try {
      import scala.jdk.CollectionConverters._
      reader.iterator().asScala.to(List)
    } finally {
      reader.close()
    }
  }

}

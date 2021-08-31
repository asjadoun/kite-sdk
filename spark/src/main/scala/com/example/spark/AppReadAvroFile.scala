package com.example.spark

import org.apache.spark.sql.{SaveMode, SparkSession}

object AppReadAvroFile {

  def main(args: Array[String]): Unit = {
    println("Starting spark application...")
      val dataFile = "test-data.json"
//    val dataFile = "test-data-multi-line.json"
//      val dataFile = "test-data-multi-line-array.json"

    val path = "./build//test-data.avro"

    val spark = SparkSession
      .builder
      .master("local[1]")
      .appName("Read avro")
      .getOrCreate()

    val data = spark.read.format("avro").load(path)

    data.show()

    spark.stop()
  }
}

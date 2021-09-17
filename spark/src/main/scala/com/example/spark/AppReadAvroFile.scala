package com.example.spark

import org.apache.spark.sql.{SaveMode, SparkSession}

object AppReadAvroFile {

  def main(args: Array[String]): Unit = {
    println("Starting spark application...")

    val path = "./build//test-data.avro"

    val spark = SparkSession
      .builder
      .master("local[1]")
      .appName("Read avro")
      .getOrCreate()

    spark.sql("set spark.sql.files.ignoreCorruptFiles=true")

    val data = spark.read
      .option("pathGlobFilter", "*.avro")
      .format("avro")
      .load(path)

    data.show()

    spark.stop()
  }
}

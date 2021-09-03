package com.example.spark

import org.apache.spark.sql.SparkSession

import java.nio.file.{Files, Paths}

object AppReadAvroFileWithExtSchema {

  def main(args: Array[String]): Unit = {
    println("Starting spark application...")

    val schemaPath = getClass.getClassLoader.getResource("one-attribute-with-max-values.avsc").getPath
    val schema = new String(Files.readAllBytes(Paths.get(schemaPath)))

    val path = getClass.getClassLoader.getResource("one-attribute-with-max-values.avro").getPath

    val spark = SparkSession
      .builder
      .master("local[1]")
      .appName("Read avro")
      .getOrCreate()

    val data = spark.read.format("avro").option("avroSchema", schema).load(path)

    data.show()

    spark.stop()
  }
}

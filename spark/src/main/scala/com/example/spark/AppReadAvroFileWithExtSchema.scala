package com.example.spark

import com.example.spark.App.getClass
import org.apache.spark.sql.SparkSession

import java.nio.file.{Files, Paths}

object AppReadAvroFileWithExtSchema {

  def main(args: Array[String]): Unit = {
    println("Starting spark application...")

    val schemaPath = getClass.getClassLoader.getResource("user-schema-complex-with-null.avsc").getPath
    val schema = new String(Files.readAllBytes(Paths.get(schemaPath)))

    val path = "./build/test-data.avro"

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

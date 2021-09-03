package com.example.spark

import com.example.spark.AppReadAvroFileWithExtSchema.getClass
import org.apache.spark.sql.{SaveMode, SparkSession}

import java.nio.file.{Files, Paths}

object AppWriteToAvro {

  def main(args: Array[String]): Unit = {
    println("Starting spark application...")
//      val dataFile = "test-data.json"
//    val dataFile = "test-data-complex.json"
//    val dataFile = "test-data-multi-line.json"
//      val dataFile = "test-data-multi-line-array.json"
val dataFile = "one-attribute-with-max-values.json"

    val path = getClass.getClassLoader.getResource(dataFile).getPath

    val schemaPath = getClass.getClassLoader.getResource("one-attribute-with-max-values.avsc").getPath
    val schema = new String(Files.readAllBytes(Paths.get(schemaPath)))

    val spark = SparkSession
      .builder
      .master("local[1]")
      .appName("Read/Write avro")
      .getOrCreate()

    val data = spark.read.format("json").load(path)

    data.show()

//    data.write.mode(SaveMode.Overwrite).format("csv").save("./build/test-data.csv")
//    data.write.mode(SaveMode.Overwrite).format("parquet").save("./build/test-data.parquet")
    data.write.mode(SaveMode.Overwrite)
      .format("avro")
//      .option("avroSchema", schema)
      .save("./build/test-data.avro")

    spark.stop()
  }
}

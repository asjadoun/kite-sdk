package com.example.spark

import org.apache.spark.sql.{SaveMode, SparkSession}

object App {

  def main(args: Array[String]): Unit = {
    println("Starting spark application...")
      val dataFile = "test-data.json"
//    val dataFile = "test-data-multi-line.json"
//      val dataFile = "test-data-multi-line-array.json"

    val path = getClass.getClassLoader.getResource(dataFile).getPath
//    val path = s"spark/src/main/resources/$dataFile"

    val spark = SparkSession
      .builder
      .master("local[1]")
      .appName("Read/Write avro")
      .getOrCreate()

    val data = spark.read.format("json").load(path)

    data.show()

    data.write.mode(SaveMode.Overwrite).format("csv").save("./build//test-data.csv")
    data.write.mode(SaveMode.Overwrite).format("parquet").save("./build//test-data.parquet")
    data.write.mode(SaveMode.Overwrite).format("avro").save("./build//test-data.avro")

    spark.stop()
  }
}

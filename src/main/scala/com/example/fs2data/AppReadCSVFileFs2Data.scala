package com.example.fs2data

import cats.effect._
import cats.effect.unsafe.implicits.global
import fs2.data.csv
import fs2.data.csv.{QuoteHandling, lowlevel}
import fs2.io.file.Files
import fs2.{Stream, text}
import org.apache.avro.Schema

import java.nio.file.Paths

object AppReadCSVFileFs2Data extends IOApp {

  override def run(args: List[String]): IO[ExitCode] = {

    import com.example.serialize.serialization._
    val stream =
      for {
        schema <- Stream(
          (new Schema.Parser).parse(
            """{
              |	"type": "record",
              |	"name": "test",
              |	"namespace": "com.tech.avro",
              |	"fields": [
              |		{
              |			"name": "myFieldA",
              |			"type": "int"
              |		},
              |		{
              |			"name": "myFieldB",
              |			"type": "string"
              |		}
              |	]
              |}""".stripMargin)
        )
        headers <- Stream("myFieldA,myFieldB")
        path <- Stream(
//          Paths.get("C:\\Users\\Anil\\Google Drive\\mymooc-workspace\\jsonExample\\src\\main\\resources\\test-data.csv")
        Paths.get("/Users/asingh/Google Drive/mymooc-workspace/jsonExample/src/main/resources/test-data.csv")
        )
        input <- Stream(
          """
            |myFieldA,myFieldB
            |1,"a"
            |2,"b"
            |3,"c"""".stripMargin).covary[IO]
        res <-
//          Files[IO]
//            .readAll(path, 1024)
//            .through(text.utf8Decode)
//            .through(text.lines)
          Stream(input)
            .through(lowlevel.rows[IO, String](separator = ',', QuoteHandling.Literal))
            .map(x => x)
//            .map(json => fromJsonToAvro(json, schema))
//            .map(avro => fromAvroToJson(avro, schema, true))
            .map(println)
      } yield res

    stream.compile.drain.unsafeRunSync()

    IO(ExitCode.Success)
  }
}

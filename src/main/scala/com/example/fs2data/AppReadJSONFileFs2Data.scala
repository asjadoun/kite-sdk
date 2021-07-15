package com.example.fs2data

import cats.effect._
import cats.effect.unsafe.implicits.global
import fs2.data.json.{render, tokens}
import fs2.io.file.Files
import fs2.{Collector, Stream, text}
import org.apache.avro.Schema

import java.nio.file.Paths

object AppReadJSONFileFs2Data extends IOApp {

  override def run(args: List[String]): IO[ExitCode] = {

    import com.example.serialize.serialization._
    import fs2.data.json.selector._

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
        path <- Stream(
          Paths.get("C:\\Users\\Anil\\Google Drive\\mymooc-workspace\\jsonExample\\src\\main\\resources\\test-data-multi-line.json")
        )
        res <-
          Files[IO]
            .readAll(path, 1024)
            .through(text.utf8Decode)
            .through(text.lines)
            .through(tokens[IO, String])
            .through(render.pretty())
//            .map(json => fromJsonToAvro(json, schema))
//            .map(avro => fromAvroToJson(avro, schema, true))
            .map(println)
      } yield res

    stream.compile.drain.unsafeRunSync()

    IO(ExitCode.Success)
  }
}

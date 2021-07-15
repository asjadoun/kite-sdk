package com.example.json4s

import cats.effect._
import cats.effect.unsafe.implicits.global
import fs2.io.file.Files
import fs2.{Stream, text}
import org.json4s.native.JsonMethods._
import java.nio.file.Paths

object AppReadFileJson4s extends IOApp {

  override def run(args: List[String]): IO[ExitCode] = {

    val stream =
      for {
        path <- Stream(Paths.get("C:\\Users\\Anil\\Google Drive\\mymooc-workspace\\jsonExample\\src\\main\\resources\\test-data.json")
        )
        res <- Files[IO]
          .readAll(path, 1024)
          .through(text.utf8Decode)
          .through(text.lines)
          .map(s => parse(s))
          .map(j => j.values)
          .map(println)
      } yield res

    stream.compile.drain.unsafeRunSync()

    IO(ExitCode.Success)
  }
}

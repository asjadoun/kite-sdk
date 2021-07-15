package com.example.ujson

import cats.effect._
import cats.effect.unsafe.implicits.global
import cats.implicits._
import fs2.Stream
import fs2.text
import fs2.io.file.Files
import ujson.StringRenderer

import java.nio.file.Paths

object AppReadJsonFileUJson extends IOApp {

  override def run(args: List[String]): IO[ExitCode] = {
//    Stream.emit("Good Morning").covary[IO].map(println).compile.drain.unsafeRunSync()
//    IO(ExitCode.Success)
//      val prg =
//          for {
//            str <- Stream.emit("""[{"myFieldA":1,"myFieldB":"g"},{"myFieldA":2,"myFieldB":"k"}]""")
//            json <- Stream.eval(Sync[IO].delay(ujson.read(str)))
//            res <- Stream.eval(IO(println(json.render())))
//          } yield res
//
//    prg.compile.drain.unsafeRunSync()

    val stream =
      for {
        path <- Stream.emit(Paths.get("C:\\Users\\Anil\\Google Drive\\mymooc-workspace\\jsonExample\\src\\main\\resources\\single.json"))
        res <- Files[IO]
          .readAll(path, 1024)
          .through(text.utf8Decode)
//          .map(s => ujson.transform(s, StringRenderer()))
          .map(j => ujson.read(j).render())
          .map(println)
      } yield res

    stream.compile.drain.unsafeRunSync()

    IO(ExitCode.Success)
  }
}

package com.example.write

import better.files.{File, using}
import org.apache.avro.Schema
import org.apache.avro.file.DataFileWriter
import org.apache.avro.generic.{GenericDatumWriter, GenericRecord}

import java.io.{BufferedOutputStream, FileOutputStream}
import java.nio.ByteBuffer

object AvroWriter {
  def toAvro(file: File, records: List[ByteBuffer], schema: Schema): Unit = {

    if (file.exists) file.delete()

    using(new BufferedOutputStream(new FileOutputStream((file.createFileIfNotExists(true)).toJava))) {
      bos =>
        using(new DataFileWriter[GenericRecord](new GenericDatumWriter[GenericRecord](schema))) {
          writer =>
            writer.create(schema, bos)
            records.foreach(record => writer.appendEncoded(record))
            bos.flush()
        }
    }
  }
}

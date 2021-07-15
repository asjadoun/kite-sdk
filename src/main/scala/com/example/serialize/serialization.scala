package com.example.serialize

import org.apache.avro.Schema
import org.apache.avro.generic.{GenericDatumReader, GenericDatumWriter, GenericRecord}
import org.apache.avro.io.{DecoderFactory, EncoderFactory}

import java.io.{ByteArrayInputStream, ByteArrayOutputStream, DataInputStream}
import java.nio.charset.StandardCharsets

object serialization {
  def fromJsonToAvro(json: String, schema: Schema) = {

    val inputStream = new ByteArrayInputStream(json.getBytes)
    val dataInputStream = new DataInputStream(inputStream)

    val jsonDecoder = DecoderFactory.get.jsonDecoder(schema, dataInputStream)
    val jsonReader = new GenericDatumReader[GenericRecord](schema)
    val jsonRecord = jsonReader.read(null, jsonDecoder)

    val outputStream = new ByteArrayOutputStream
    val binaryEncoder = EncoderFactory.get.binaryEncoder(outputStream, null)

    val jsonWriter = new GenericDatumWriter[GenericRecord](schema)
    jsonWriter.write(jsonRecord, binaryEncoder)
    binaryEncoder.flush()

    outputStream.toByteArray
  }

  def fromAvroToJson(avroBytes: Array[Byte], schema: Schema, includeNamespace: Boolean) = {
    val avroReader = new GenericDatumReader[GenericRecord](schema)
    val outputStream = new ByteArrayOutputStream

    val avroDecoder = DecoderFactory.get.binaryDecoder(avroBytes, null)
    val datum = avroReader.read(null, avroDecoder)
    val jsonWriter = new GenericDatumWriter[GenericRecord](schema)
    val jsonEncoder = EncoderFactory.get().jsonEncoder(schema, outputStream)

//    jsonEncoder.setIncludeNamespace(includeNamespace)
    jsonWriter.write(datum, jsonEncoder)
    jsonEncoder.flush()
    outputStream.flush()
    new String(outputStream.toByteArray, StandardCharsets.UTF_8.name)
  }
}
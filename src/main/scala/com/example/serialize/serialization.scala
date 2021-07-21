package com.example.serialize

import org.apache.avro.Schema
import org.apache.avro.generic.{GenericDatumReader, GenericDatumWriter, GenericRecord}
import org.apache.avro.io.{BinaryDecoder, BinaryEncoder, DecoderFactory, EncoderFactory, JsonDecoder, JsonEncoder}

import java.io.{ByteArrayInputStream, ByteArrayOutputStream, DataInputStream}
import java.nio.ByteBuffer
import java.nio.charset.StandardCharsets

object serialization {
  def fromJsonToByteBuffer(json: String, schema: Schema): ByteBuffer = {
    val record: GenericRecord = fromJsonToGenericRecord(json, schema: Schema)
    val bytes: Array[Byte] = record.toString.getBytes(StandardCharsets.UTF_8)
    val buf: ByteBuffer = ByteBuffer.wrap(bytes)
    buf
  }
  def fromJsonToGenericRecord(json: String, schema: Schema): GenericRecord = {
    val inputStream = new ByteArrayInputStream(json.getBytes)
    try {
      val dataInputStream = new DataInputStream(inputStream)

      try {
        val jsonDecoder: JsonDecoder = DecoderFactory.get.jsonDecoder(schema, dataInputStream)
        val jsonReader = new GenericDatumReader[GenericRecord](schema)
        val genericRecord: GenericRecord = jsonReader.read(null, jsonDecoder)
        genericRecord

      } finally {
        dataInputStream.close()
      }
    } finally {
      inputStream.close()
    }
  }

  def fromJsonToAvro(json: String, schema: Schema): Array[Byte] = {
    try {
      val genericRecord: GenericRecord = fromJsonToGenericRecord(json, schema)
      val outputStream = new ByteArrayOutputStream
      try {
        val binaryEncoder: BinaryEncoder = EncoderFactory.get.binaryEncoder(outputStream, null)
        val jsonWriter = new GenericDatumWriter[GenericRecord](schema)
        jsonWriter.write(genericRecord, binaryEncoder)
        binaryEncoder.flush()

        outputStream.toByteArray
      } finally {
        outputStream.close()
      }
    }
  }

  def fromAvroToJson(avroBytes: Array[Byte], schema: Schema, includeNamespace: Boolean): String = {
    val outputStream = new ByteArrayOutputStream
    try {
      val avroReader = new GenericDatumReader[GenericRecord](schema)
      val avroDecoder: BinaryDecoder = DecoderFactory.get.binaryDecoder(avroBytes, null)
      val datum: GenericRecord = avroReader.read(null, avroDecoder)
      val jsonWriter = new GenericDatumWriter[GenericRecord](schema)
      val jsonEncoder: JsonEncoder = EncoderFactory.get().jsonEncoder(schema, outputStream)
//      jsonEncoder.setIncludeNamespace(includeNamespace)
      jsonWriter.write(datum, jsonEncoder)
      jsonEncoder.flush()
      outputStream.flush()

      new String(outputStream.toByteArray, StandardCharsets.UTF_8.name)
    } finally {
      outputStream.close()
    }
  }
}
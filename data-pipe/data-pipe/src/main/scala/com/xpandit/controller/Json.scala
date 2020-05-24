package com.xpandit.controller

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.databind.module.SimpleModule
import com.fasterxml.jackson.databind.{DeserializationFeature, JsonNode, ObjectMapper}
import com.fasterxml.jackson.module.scala.DefaultScalaModule

object Json {

  @transient lazy val instance: ObjectMapper = {
    val simpleModule = new SimpleModule()

    val mapper = new ObjectMapper()
    mapper.registerModule(DefaultScalaModule)
    mapper.registerModule(simpleModule)
    mapper.disable(DeserializationFeature.FAIL_ON_NULL_FOR_PRIMITIVES)
    mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
    mapper.enable(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT)
    mapper.enable(DeserializationFeature.WRAP_EXCEPTIONS)
    mapper.enable(DeserializationFeature.ACCEPT_EMPTY_ARRAY_AS_NULL_OBJECT)
    mapper.enable(DeserializationFeature.USE_BIG_DECIMAL_FOR_FLOATS)
    mapper.setSerializationInclusion(JsonInclude.Include.ALWAYS)
    mapper
  }

  def read[T](str: String, clazz: Class[T]): T = instance.readValue(str, clazz)

  def prettyPrint(ref: Any): String = instance.writerWithDefaultPrettyPrinter.writeValueAsString(ref)

  def stringify(ref: Any): String = instance.writeValueAsString(ref)

}

package com.xpandit.controller

import java.io.InputStream

import scala.io.Source

class DataPipeConfigs(val fileName : String) {

  var configs : Map[String,String] = Map[String,String]()
  extractConfigs

  def extractConfigs = {
    val stream : InputStream = getClass.getClassLoader.getResourceAsStream(fileName)
    val lines : Iterator[String] = Source.fromInputStream(stream)
      .getLines()
    lines.foreach( line => {
      val kv = line.split("=")
      configs += (kv(0) -> kv(1))
    })
  }
}

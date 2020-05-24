package com.xpandit.controller

import java.util.concurrent.TimeUnit

import org.apache.spark.sql.streaming.{OutputMode, StreamingQuery, Trigger}
import org.apache.spark.sql.{DataFrame, Dataset, SparkSession}


class Session(val config : DataPipeConfigs = new DataPipeConfigs("test.properties"), val sparkSession: SparkSession) {

  val BOOTSTRAP_SERVER = "quickstart.cloudera:"
  val PORT = "9092"

  def this(config: DataPipeConfigs){
    this(config, SparkSession
      .builder()
      .master("local[*]")
      .appName("DataPipeSpark")
      .getOrCreate())
  }


  def readStream(topic: String) : DataFrame = {
    sparkSession.readStream
      .format("kafka")
      .option("kafka.bootstrap.servers", config.configs("kafka.brokers"))
      .option("subscribe", topic)
      .option("startingOffsets", "earliest")
      .load()
  }

  def writeStream(outTopic: String,df : Dataset[String]): StreamingQuery = {
    df
      .writeStream
      .format("kafka")
      .option("kafka.bootstrap.servers", config.configs("kafka.brokers"))
      .option("topic", outTopic)
      .option("checkpointLocation", "/tmp/t1")
      .queryName("Data Transformation Streaming Query")
      .outputMode(OutputMode.Append())
      .trigger(Trigger.ProcessingTime(TimeUnit.SECONDS.toMillis(2)))
      .start()
  }


}

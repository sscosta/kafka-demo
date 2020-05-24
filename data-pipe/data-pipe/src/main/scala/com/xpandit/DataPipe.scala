package com.xpandit

import com.xpandit.controller.{DataPipeConfigs, IO, Json, Queries, Session}
import org.slf4j.LoggerFactory

/**
 * @author ${user.name}
 */

object DataPipe {

  def main(args : Array[String]): Unit = {
    val log = LoggerFactory.getLogger(DataPipe.getClass)
    log.info("Initializing Kafka -> Spark -> Kafka App")
    // intialization
    val config : DataPipeConfigs = new DataPipeConfigs("application.properties")
    val s : Session = new Session(config)
    val io : IO = new IO(s)
    val queries : Queries = new Queries(s)

    // Read From Kafka
    import s.sparkSession.implicits._
    val inTopic = config.configs("kafka.input.topic")
    val df = io.readFromTopic(inTopic).selectExpr("CAST(value AS STRING)")
      .as[String]

    // Transformation
    val result = queries.transform(df)

    // Write to Kafka
    val outTopic = config.configs("kafka.output.topic")
    val dfr = io.writeToTopic(outTopic,result).awaitTermination()

    log.info("Terminating Kafka -> Spark -> Kafka App")
  }
}

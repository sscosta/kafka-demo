package com.xpandit.controller

import org.apache.spark.sql.streaming.StreamingQuery
import org.apache.spark.sql.{DataFrame, Dataset}

class IO(val session: Session) {
  def writeToTopic(outTopic: String, df: Dataset[String]): StreamingQuery = {
    session.writeStream(outTopic, df)
  }

  def readFromTopic(inTopic: String) : DataFrame = {
    session.readStream(inTopic)
  }
}

package com.xpandit.controller

import com.xpandit.model.KeyValuePair
import org.apache.spark.sql.{Dataset}

import scala.util.{Failure, Success, Try}

class Queries(s: Session) {
  def transform(ds: Dataset[String]) : Dataset[String] = {
    import s.sparkSession.implicits._
    ds.flatMap{
      record =>
        Try(Json.read(record, classOf[KeyValuePair])) match {
          case Success(recordObject) => Some(Json.stringify(recordObject.toTriple()))
          case Failure(exception) =>
            System.out.println(s"Error deserializing '$record' with error $exception")
            None
        }
    }
  }
}

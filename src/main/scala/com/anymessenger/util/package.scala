package com.anymessenger

import java.sql.Timestamp
import java.text.SimpleDateFormat
import java.util.Date

import com.typesafe.scalalogging.Logger

import scala.concurrent.{ExecutionContext, Future}

package object util {

  val DEFAULT_DATE_MASK = "yyyy-MM-dd"
  val DEFAULT_TIMESTAMP_MASK = "yyyy-MM-dd HH:mm:ss"

  val defaultDateFormatter = new SimpleDateFormat(DEFAULT_DATE_MASK)
  val defaultTimestampFormat = new SimpleDateFormat(DEFAULT_TIMESTAMP_MASK)

  def nowTimestamp = new Timestamp(new Date().getTime)

  def logTime[T](msg: String)(f: => T)(implicit logger: Logger): T = {
    val start = System.currentTimeMillis
    logger.debug(s"$msg - Started")
    val result = f
    logger.debug(s"$msg - Finished (took ${System.currentTimeMillis - start} ms)")
    result
  }

  def logTimeF[T](msg: String)(f: => Future[T])(implicit ec: ExecutionContext, logger: Logger): Future[T] = {
    val start = System.nanoTime()
    logger.debug(s"$msg - Started")

    f
      .map {
        result =>
          logger.trace(s"$msg - Result: $result")
          logger.debug(s"$msg - Finished (took ${(System.nanoTime() - start) / 1e6} ms)")
          result
      }
      .recover {
        case e: Throwable =>
          logger.error(s"$msg - Finished thrown exception (took ${(System.nanoTime() - start) / 1e6} ms)", e)
          throw e
      }
  }

}

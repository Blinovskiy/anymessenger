import java.text.SimpleDateFormat

import org.slf4j.Logger

import scala.concurrent.{ExecutionContext, Future}

package object util {

  val DEFAULT_DATE_MASK = "yyyy-MM-dd"

  val defaultDateFormatter = new SimpleDateFormat(DEFAULT_DATE_MASK)

  def logTime[T](msg: String)(f: => Future[T])(implicit ec: ExecutionContext, logger: Logger): Future[T] = {
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

  //  def logTime[T](msg: String)(f: => Future[T])(implicit ec: ExecutionContext, logger: Logger): Future[T] =
  //    Future {
  //      if (logger.isDebugEnabled || logger.isTraceEnabled)
  //        Some(System.nanoTime())
  //      else
  //        None
  //    }
  //      .flatMap {
  //        case None =>
  //          f
  //        case Some(start) =>
  //          logger.debug(s"$msg - Started")
  //
  //          f
  //            .map {
  //              result =>
  //                if (logger.isTraceEnabled)
  //                  logger.trace(s"$msg - Result: $result")
  //
  //                logger.debug(s"$msg - Finished (took ${(System.nanoTime() - start) / 1e6} ms)")
  //
  //                result
  //            }
  //            .recover {
  //              case e: Throwable =>
  //                if (logger.isTraceEnabled)
  //                  logger.error(s"$msg - Finished thrown exception (took ${(System.nanoTime() - start) / 1e6} ms)", e)
  //                else
  //                  logger.debug(s"$msg - Finished thrown exception (took ${(System.nanoTime() - start) / 1e6} ms)")
  //
  //                throw e
  //            }
  //
  //      }
}

package service

import com.anymessenger.db.AnymessengerDBObjects
import db.DBConfig
import org.slf4j.{Logger, LoggerFactory}

import scala.concurrent.Await
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration._

object CheckDB
  extends App
    with DBConfig
    with AnymessengerDBObjects {

  import Logic._

  private val logger: Logger = LoggerFactory.getLogger(Logic.getClass)
  //  override def schema = Some("ANY_DEV")

  //  implicit val any2str: Any => String = a => a.toString
  //  implicit def any2str(a: Any): String = a.toString
  //  logger.info(any2str(config.config))


  test()

  val user2 = Await.result(getUser(2), 5 second)
  logger.info(user2.toString)

  logger.error("error")
  logger.warn("warn")
  logger.info("info")
  logger.debug("debug")
  logger.trace("trace")
}
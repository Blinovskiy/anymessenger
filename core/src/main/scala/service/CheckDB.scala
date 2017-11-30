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

  test()

  val user2 = Await.result(getUser(2), 5 second)
  logger.info(user2.toString)
}
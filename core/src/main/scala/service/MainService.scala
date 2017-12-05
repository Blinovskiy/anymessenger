package service

import java.util.Date

import com.anymessenger.db.AnymessengerDBObjects
import com.anymessenger.db.projection.{MessageRow, UserRow}
import com.twitter.finagle.Http
import com.twitter.util.Await
import db.{DBConfig, Logic}
import io.circe._
import io.circe.generic.auto._
import io.finch._
import io.finch.circe.dropNullValues._
import org.slf4j.{Logger, LoggerFactory}
import util.logTime

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.{Await => SAwait, Future => SFuture}
import scala.util.{Failure, Success, Try}

object MainService
  extends App
    with DBConfig
    with AnymessengerDBObjects {

  import Logic._

  private implicit val logger: Logger = LoggerFactory.getLogger(Logic.getClass)
  //  override def schema = Some("ANY_DEV")

  implicit val dateEncoder: Encoder[Date] = Encoder.instance(a => Json.fromLong(a.getTime))
  implicit val dateDecoder: Decoder[Date] = Decoder.instance(a => a.as[Long].map(new Date(_)))

  //  implicit val any2str: Any => String = a => a.toString
  //  implicit def any2str(a: Any): String = a.toString
  //  logger.info(any2str(config.config))

  getOrCreateUserAndMessages()
  val user2 = SAwait.result(getUser(3), futureWaitTimeout)
  logger.info(user2.toString)

  logTime(s"test logTime") {
    SFuture {3 + 2}
  }


  def wrap[T](future: SFuture[Either[String, T]])(isEmpty: T => Boolean): SFuture[Output[T]] = {
    future.map { future =>
      Try(future) match {
        case Success(Right(l)) if !isEmpty(l) => Ok(l)
        case Success(Right(_)) => NoContent
        case Success(Left(msg)) => InternalServerError(new Exception(msg))
        case Failure(e) => InternalServerError(new Exception(e.getMessage))
      }
    }
  }

  import io.finch.syntax.scala._

  val user: Endpoint[Option[UserRow]] = get("user" :: path[Long]) { userId: Long => wrap(getUser(userId))(_.isEmpty) }
  val message: Endpoint[Option[MessageRow]] = get("message" :: path[Long]) { messageId: Long => wrap(getMessage(messageId))(_.isEmpty) }
  val messages: Endpoint[List[MessageRow]] = get("messages" :: path[Long]) { userId: Long => wrap(getUserMessages(userId))(_.isEmpty) }
  Await.ready(
    Http.server.serve(":8081", (user :+: message :+: messages).toService)
  )

}

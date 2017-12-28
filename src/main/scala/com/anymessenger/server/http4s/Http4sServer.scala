package com.anymessenger.server.http4s

import java.sql.Timestamp

import cats.Eval
import cats.effect.IO
import cats.implicits._
import io.circe.syntax._
import io.circe.{Decoder, Encoder, Json}
import org.http4s.circe._
import org.http4s.dsl.Http4sDsl
import org.http4s.server.blaze.BlazeBuilder
import org.http4s.util.{ExitCode, StreamApp}
import org.http4s.{HttpService, Response}
//import org.http4s.dsl.io._
//import org.http4s.implicits._
//import org.http4s.client._

import com.anymessenger.service.helpers.HelpService._
import com.anymessenger.service.impl.MainServiceImpl._
import config.TypesafeConfig._
import io.circe.generic.auto._
import util.defaultTimestampFormat

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.util.{Failure, Success, Try}

object Http4sServer extends StreamApp[IO] with Http4sDsl[IO] {

  //  implicit val dateEncoder: Encoder[Date] = Encoder.instance(a => Json.fromLong(a.getTime))
  //  implicit val dateDecoder: Decoder[Date] = Decoder.instance(a => a.as[Long].map(new Date(_)))

  //  implicit val timestampEncoder: Encoder[Timestamp] = Encoder.instance(a => Json.fromLong(a.getTime))
  //  implicit val timestampDecoder: Decoder[Timestamp] = Decoder.instance(a => a.as[Long].map(new Timestamp(_)))

  /** Implicit [[io.circe.Json]] to [[java.sql.Timestamp]] converter */
  implicit final val encodeTimestampObject: Encoder[java.sql.Timestamp] =
    new Encoder[java.sql.Timestamp] {
      final def apply(a: java.sql.Timestamp): Json =
        Json.fromString(a.toString)
    }
  /** Implicit [[java.sql.Timestamp]] to [[io.circe.Json]] converter */
  implicit val decodeTimestamp: Decoder[Timestamp] =
    Decoder.decodeString.emap { str =>
      Either
        .catchNonFatal(new Timestamp(defaultTimestampFormat.parse(str).getTime))
        .leftMap(t => "Timestamp")
    }


  // todo:
  //  def wrap[T](future: Future[Either[String, T]])(isEmpty: T => Boolean)(implicit enc: Encoder[T]): IO[Response[IO]]
  def wrap[T: Encoder](future: Future[Either[String, T]])(isEmpty: T => Boolean): IO[Response[IO]] = {
    IO.fromFuture(Eval.always(future.map {
      Try(_) match {
        case Success(Right(l)) if !isEmpty(l) => Ok(l.asJson)
        case Success(Right(_)) => NoContent()
        case Success(Left(msg)) => InternalServerError()
        case Failure(e) => InternalServerError()
      }
    })).flatten
  }

  val services: HttpService[IO] = HttpService[IO] {
    case GET -> Root / "user" / LongVar(userId) => wrap(getUser(userId))(_.isEmpty)
    case GET -> Root / "message" / LongVar(messageId) => wrap(getMessage(messageId))(_.isEmpty)
    case GET -> Root / "messages" / LongVar(userId) => wrap(getUserMessages(userId))(_.isEmpty)

    case GET -> Root / "init" => Ok(getOrCreateUserAndMessages())

    case POST => MethodNotAllowed()
    case PUT => MethodNotAllowed()
    case _ => NotFound()
  }

  def stream(args: List[String], requestShutdown: IO[Unit]): fs2.Stream[IO, ExitCode] =
    BlazeBuilder[IO]
      .bindHttp(port, interface)
      .mountService(services, "/api")
      .serve
}

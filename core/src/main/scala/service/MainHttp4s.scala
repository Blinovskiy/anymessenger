package service

import cats.Eval
import cats.effect._
import cats.implicits._
import io.circe._
import io.circe.literal._
import io.circe.syntax._
import org.http4s._
//import org.http4s.dsl.io._
//import org.http4s.implicits._
//import org.http4s.client._
import java.util.Date

import db.Logic
import org.http4s.circe._
import org.http4s.dsl.Http4sDsl
import org.http4s.server.blaze.BlazeBuilder
import org.http4s.util.{ExitCode, StreamApp}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.util.{Failure, Success, Try}

object MainHttp4s extends StreamApp[IO] with Http4sDsl[IO] {

  import Logic._
  import io.circe.generic.auto._

  implicit val dateEncoder: Encoder[Date] = Encoder.instance(a => Json.fromLong(a.getTime))
  implicit val dateDecoder: Decoder[Date] = Decoder.instance(a => a.as[Long].map(new Date(_)))

//def wrap[T](future: Future[Either[String, T]])(isEmpty: T => Boolean)(implicit enc: Encoder[T]): IO[Response[IO]] = {
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
  //InternalServerError(new Exception(msg))
  //InternalServerError(new Exception(e.getMessage))

  val services: HttpService[IO] = HttpService[IO] {
    case GET -> Root / "user" / LongVar(userId) => wrap(getUser(userId))(_.isEmpty)
    case GET -> Root / "message" / LongVar(messageId) => wrap(getMessage(messageId))(_.isEmpty)
    case GET -> Root / "messages" / LongVar(userId) => wrap(getUserMessages(userId))(_.isEmpty)

    case GET -> Root / "init" => Ok(getOrCreateUserAndMessages())

    case POST => MethodNotAllowed()
    case PUT => MethodNotAllowed()
    case _ => NotFound()
    //case _ => IO(Response(Status.Ok))
  }

  def stream(args: List[String], requestShutdown: IO[Unit]): fs2.Stream[IO, ExitCode] =
    BlazeBuilder[IO]
      .bindHttp(8081, "0.0.0.0")
      .mountService(services, "/")
      //.mountService(services, "/api")
      .serve
}













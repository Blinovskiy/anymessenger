package com.anymessenger.server.http4s

import java.sql.Timestamp
import java.util.Date

import cats.data.NonEmptyList
import cats.effect.IO
import io.circe.{Decoder, Encoder, Json}
import org.http4s.CacheDirective.`no-cache`
import org.http4s.dsl.Http4sDsl
import org.http4s.headers.`Cache-Control`
import org.http4s.server.blaze.BlazeBuilder
import org.http4s.util.{ExitCode, StreamApp}
import org.http4s.{Cookie, HttpService}
//import org.http4s.dsl.io._
//import org.http4s.implicits._
//import org.http4s.client._
import cats.Eval
import com.anymessenger.model.Tables._
import io.circe.generic.auto._
import io.circe.literal._
import io.circe.syntax._
import org.http4s.circe._

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

object CheckHttp4s extends StreamApp[IO] with Http4sDsl[IO] {


  implicit val dateEncoder: Encoder[Date] = Encoder.instance(a => Json.fromLong(a.getTime))
  implicit val dateDecoder: Decoder[Date] = Decoder.instance(a => a.as[Long].map(new Date(_)))

  case class User(name: String, year: Int)

  val user = User("Serj", 24)

  case class UserOpt(name: Option[String], year: Option[Int], date: Option[Date])

  val userOpt = UserOpt(Some("Serj"), Some(24), Some(new Date))

  val urow = UserRow(
    id = -1L,
    firstname = Some("TestFN"),
    lastname = Some("TestLN"),
    login = Some("TestLogin"),
    email = Some("TestEMAIL"),
    gender = Some(true), // false - fm , true - m
    description = None,
    isactive = true,
    createdat = Some(new Timestamp(new Date().getTime)),
    updatedat = None,
    deletedat = None,
    isdeleted = false
  )

  val service: HttpService[IO] = HttpService[IO] {
    case GET -> Root / "hello" / name => Ok(Json.obj("message" -> Json.fromString(s"Hello, $name")))
    case GET -> Root / "user" / IntVar(userId) => Ok(user.asJson)
    case GET -> Root / "userOpt" / IntVar(userId) => Ok(userOpt.asJson)
    case GET -> Root / "future" => Ok(IO.fromFuture(Eval.always(Future("Hello from the future!")))) // EntityEncoder allows rendering asynchronous results as well
    case GET -> Root / "cache" => Ok("Ok response.", `Cache-Control`(NonEmptyList(`no-cache`(), Nil)))
    case GET -> Root / "cookie" => Ok("Ok response.").map(_.addCookie(Cookie("foo", "bar")))
  }

  def stream(args: List[String], requestShutdown: IO[Unit]): fs2.Stream[IO, ExitCode] =
    BlazeBuilder[IO]
      .bindHttp(8081, "0.0.0.0")
      .mountService(service, "/")
      .serve
}

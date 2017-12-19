package service

import java.util.Date

import cats.Eval
import cats.data.NonEmptyList
import cats.effect.IO
import com.anymessenger.db.projection.UserRow
import io.circe._
import org.http4s.CacheDirective.`no-cache`
import org.http4s._
import org.http4s.circe._
import org.http4s.dsl.Http4sDsl
import org.http4s.headers.`Cache-Control`
import org.http4s.server.blaze.BlazeBuilder
import org.http4s.util.{ExitCode, StreamApp}
import org.http4s.headers.`Cache-Control`
import org.http4s.CacheDirective.`no-cache`
import cats.data.NonEmptyList
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.util.{Failure, Success, Try}

object CheckHttp4s extends StreamApp[IO] with Http4sDsl[IO] {

  import io.circe.syntax._
  import io.circe.generic.auto._

  implicit val dateEncoder: Encoder[Date] = Encoder.instance(a => Json.fromLong(a.getTime))
  implicit val dateDecoder: Decoder[Date] = Decoder.instance(a => a.as[Long].map(new Date(_)))

  case class User(name: String, year: Int)
  val user = User("Serj", 24)

  case class UserOpt(name: Option[String], year: Option[Int], date: Option[Date])
  val userOpt = UserOpt(Some("Serj"), Some(24), Some(new Date))

  val urow = UserRow(
    id = None,
    firstName = Some("TestFN"),
    lastName = Some("TestLN"),
    login = Some("TestLogin"),
    email = Some("TestEMAIL"),
    gender = Some(true), // false - fm , true - m
    description = None,
    isActive = true,
    createdAt = Some(new Date()),
    updatedAt = None,
    deletedAt = None,
    isDeleted = false
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
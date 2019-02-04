package com.anymessenger.server.http4s

import java.io.File
import java.sql.Timestamp

import cats.Foldable
import cats.effect._
import cats.implicits._
import com.anymessenger.config.TypesafeConfig
import com.anymessenger.service.model.request.{RequestMessageEntity, RequestUserEntity}
import com.anymessenger.service.model.response.{ResponseMessageEntity, ResponseUserEntity}
//import cats.syntax.foldable._

import fs2.{Stream, StreamApp}
import fs2.StreamApp.ExitCode

import org.http4s.implicits._
import org.http4s._
import org.http4s.dsl.io._
import org.http4s.circe._
import org.http4s.server.middleware.{CORS, CORSConfig}

import io.circe._
import io.circe.generic.auto._
import io.circe.syntax._

import com.anymessenger.service.helpers.HelpService._
import com.anymessenger.service.impl.MainServiceImpl._
import com.anymessenger.util.defaultTimestampFormat
import com.anymessenger.util.nowTimestamp

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.{Await, Future}

object Http4sServer extends StreamApp[IO] {

  Await.result(checkConnection(), testFutureWaitTimeout) match {
    case Right(_) => _logger.debug("DB ready")
    case Left(e) => _logger.error(e.getMessage)
    //    case Left(e) => _logger.error("Cant connect to DB")
  }


  case class MessagesRequestByDate(
                                    from: Timestamp,
                                    to: Timestamp
                                  )


  /** Implicit [[io.circe.Json]] to [[java.sql.Timestamp]] converter */
  implicit val encodeTimestampObject: Encoder[Timestamp] =
    new Encoder[Timestamp] {
      final def apply(a: Timestamp): Json = Json.fromString(a.toString)
    }
  /** Implicit [[java.sql.Timestamp]] to [[io.circe.Json]] converter */
  implicit val decodeTimestamp: Decoder[Timestamp] =
    Decoder.decodeString.emap { str =>
      Either
        .catchNonFatal(new Timestamp(defaultTimestampFormat.parse(str).getTime))
        .leftMap(_ => "Timestamp")
    }

  //  manual
  //  import io.circe.literal._
  //  implicit val userEncoder: Encoder[User] =
  //    Encoder.instance { user: User =>
  //      json"""{"firstname": ${user.firstname}, "lastname": ${user.lastname}}"""
  //    }

  implicit val mrbdDecoder: EntityDecoder[IO, MessagesRequestByDate] = jsonOf[IO, MessagesRequestByDate]
  //  implicit val mrbdEncoder: EntityEncoder[IO, MessagesRequestByDate] = jsonEncoderOf[IO, MessagesRequestByDate]

  implicit val reqMessageDecoder: EntityDecoder[IO, RequestMessageEntity] = jsonOf[IO, RequestMessageEntity]
  //  implicit val reqMessageEncoder: EntityEncoder[IO, MessageEntity] = jsonEncoderOf[IO, MessageEntity]

  implicit val respMsgDecoder: EntityDecoder[IO, ResponseMessageEntity] = jsonOf[IO, ResponseMessageEntity]
  implicit val respMsgEncoder: EntityEncoder[IO, ResponseMessageEntity] = jsonEncoderOf[IO, ResponseMessageEntity]

  implicit val reqUserDecoder: EntityDecoder[IO, RequestUserEntity] = jsonOf[IO, RequestUserEntity]
  //  implicit val reqUserEncoder: EntityEncoder[IO, RequestUserEntity] = jsonEncoderOf[IO, RequestUserEntity]

  implicit val respUserDecoder: EntityDecoder[IO, ResponseUserEntity] = jsonOf[IO, ResponseUserEntity]
  implicit val respUserEncoder: EntityEncoder[IO, ResponseUserEntity] = jsonEncoderOf[IO, ResponseUserEntity]

  import org.http4s.headers.`Cache-Control`
  import org.http4s.CacheDirective.`no-cache`
  import cats.data.NonEmptyList


  case class TestMessage(
                          text: Option[String],
                          time: Option[Timestamp],
                          isdeleted: Option[Boolean]
                        )

  implicit val simpleMessageDecoder: EntityDecoder[IO, TestMessage] = jsonOf[IO, TestMessage]
  implicit val simpleMessageEncoder: EntityEncoder[IO, TestMessage] = jsonEncoderOf[IO, TestMessage]

  implicit val timestampEncoder: EntityEncoder[IO, Timestamp] = jsonEncoderOf[IO, Timestamp]
  implicit val timestampDecoder: EntityDecoder[IO, Timestamp] = jsonOf[IO, Timestamp]

  val testService: HttpService[IO] = HttpService[IO] {
    case GET -> Root / "hello" / name =>
      Ok(s"Hello, $name.", `Cache-Control`(NonEmptyList(`no-cache`(), Nil))).map(_.addCookie(Cookie("foo", "bar"))).map(_.putHeaders(Header("X-Auth-Token", "value")))
    // Ok(s"Hello, $name.", MenuHeader("X-Auth-Token", "value"), `Cache-Control`(NonEmptyList(`no-cache`(), Nil))).map(_.addCookie(Cookie("foo", "bar")))

    case GET -> Root / "test" =>
      Ok(IO {
        println("I run when the IO is run.")
        "Mission accomplished!"
      }).map(_.removeCookie("foo"))


    case GET -> Root / "time" => Ok(nowTimestamp.asJson)

    case req@POST -> Root / "time" =>
      for {
        time <- req.as[Timestamp]
        resp <- Ok(time.asJson)
      } yield resp

    case req@POST -> Root / "message" =>
      for {
        message <- req.as[TestMessage]
        resp <- Ok(message.asJson)
      } yield resp
  }


  def f2IO[T](future: => Future[T]): IO[T] = {
    IO.fromFuture(IO {
      future
    })
  }

  //  def wrap_old[T: Encoder](io: IO[Either[Throwable, T]])(isEmpty: T => Boolean): IO[Response[IO]] = {
  //    io.flatMap {
  //      case Right(l) if !isEmpty(l) => Ok(l.asJson)
  //      case Right(_) => NoContent()
  //      case Left(e) => InternalServerError(e.getMessage)
  //    }
  //  }

  def wrap[T[_], V](io: Either[Throwable, T[V]])(implicit e: Encoder[T[V]], f: Foldable[T]): IO[Response[IO]] =
    io match {
      case Right(l) if !l.isEmpty =>
        _logger.trace(l.asJson.toString())
        Ok(l.asJson)
      case Right(_) => NoContent()
      case Left(ex) => InternalServerError(ex.getMessage)
    }

  def wrap[T: Encoder](io: Either[Throwable, T]): IO[Response[IO]] =
    io match {
      case Right(l) =>
        _logger.trace(l.asJson.toString())
        Ok(l.asJson)
      case Left(ex) => InternalServerError(ex.getMessage)
    }


  def static(fileName: String, request: Request[IO]): IO[Response[IO]] = {
    val file = new File("ui/dist/" + fileName)
    StaticFile.fromFile(file, Some(request)).getOrElseF(NotFound())
  }

  val rootService: HttpService[IO] = HttpService[IO] {
    //    case request@GET -> Root / "index.html" =>
    //      StaticFile.fromFile(new File("ui/dist/index.html"), Some(request))
    //        .getOrElseF(NotFound())

    case request@GET -> Root / path if List(".js", ".css", ".map", ".html", ".webm").exists(path.endsWith) =>
      //      println(s"path: $path")
      //      println(s"path ext exists: ${List(".js", ".css", ".map", ".html", ".webm").exists(path.endsWith)}")
      static(path, request)
  }

  val services: HttpService[IO] = HttpService[IO] {
    //    case GET -> Root / "user" / LongVar(userId) => wrap_old(f2IO(getUser(userId)))(_.isEmpty)
    case GET -> Root / "user" / LongVar(userId) => f2IO(getUser(userId)).flatMap(wrap(_))
    case GET -> Root / "message" / LongVar(messageId) => f2IO(getMessage(messageId)).flatMap(wrap(_))
    case GET -> Root / "messages" / LongVar(userId) => f2IO(getUserMessages(userId)).flatMap(wrap(_))
    case GET -> Root / "messages" => f2IO(getAllMessages()).flatMap(wrap(_))
    case GET -> Root / "messages" / "last" / IntVar(n) => f2IO(getLastNMessages(n)).flatMap(wrap(_))
    case GET -> Root / "init" => Ok(getOrCreateUserAndMessages())

    case req@POST -> Root / "messagesByDate" =>
      for {
        dates <- req.as[MessagesRequestByDate]
        resp <- f2IO(getMessagesByDate(dates.from, dates.to)).flatMap(wrap(_))
      } yield resp

    //    case req@POST -> Root / "user" =>
    //      for {
    //        user <- req.as[UserinfoRow]
    //        resp <- Ok(user.asJson)
    //      } yield resp

    //    case req@POST -> Root / "message" =>
    //      for {
    //        message <- req.as[MessageRow]
    //        resp <- Ok(message.asJson)
    //      } yield resp

    case req@POST -> Root / "user" =>
      for {
        user <- req.as[RequestUserEntity]
        resp <- f2IO(insertOrUpdateUser(user)).flatMap(wrap(_))
      } yield resp

    case req@POST -> Root / "message" =>
      for {
        message <- req.as[RequestMessageEntity]
        resp <- f2IO(insertOrUpdateMessage(message)).flatMap(wrap(_))
      } yield resp

    case POST -> Root / "message" / "delete" / LongVar(id) => f2IO(deleteMessage(id)).flatMap(wrap(_))

    // OTHER
    case PUT -> _ => MethodNotAllowed()
    //    case _ => NotFound()
  }

  val originConfig: CORSConfig = CORSConfig(
    anyOrigin = true,
    allowCredentials = false,
    maxAge = 10000L,
    anyMethod = true,
    allowedOrigins = _ => false,
    allowedMethods = None,
    allowedHeaders = Set("Content-Type", "*").some,
    exposedHeaders = Set("*").some
  )

  val rootCorsServices = CORS(rootService, originConfig)
  val corsServices = CORS(services, originConfig)

  override def stream(args: List[String], requestShutdown: IO[Unit]): Stream[IO, ExitCode] = {
    import org.http4s.server.blaze._
    BlazeBuilder[IO]
      .bindHttp(TypesafeConfig.port, TypesafeConfig.interface)
      .mountService(rootCorsServices, "/")
      .mountService(corsServices, "/api")
      .mountService(testService, "/test")
      .serve
  }
}

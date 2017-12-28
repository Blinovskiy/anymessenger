package com.anymessenger.server.finch

import java.sql.Timestamp
import java.util.Date

import com.anymessenger.model.Tables._
import com.twitter.finagle.Http
import com.twitter.util.Await
import config.TypesafeConfig
import io.circe._
import io.circe.generic.auto._
import io.finch._
//import io.finch.circe._
//import io.circe.generic.semiauto._
import io.finch.circe.dropNullValues._
import scala.concurrent.ExecutionContext.Implicits.global

object CheckFinch extends App {
  implicit val dateEncoder: Encoder[Date] = Encoder.instance(a => Json.fromLong(a.getTime))
  implicit val dateDecoder: Decoder[Date] = Decoder.instance(a => a.as[Long].map(new Date(_)))

  implicit val timestampEncoder: Encoder[Timestamp] = Encoder.instance(a => Json.fromLong(a.getTime))
  implicit val timestampDecoder: Decoder[Timestamp] = Decoder.instance(a => a.as[Long].map(new Timestamp(_)))

  //  implicit val decoder: Decoder[TestOption] = deriveDecoder[TestOption]
  //  implicit val encoder: Encoder[TestOption] = deriveEncoder[TestOption]


  //  case class TestOption(id: Long, fisrt: String, second: String)
  //  val opt: Endpoint[TestOption] =
  //    get("test" :: path[Long]) { id: Long => Ok(TestOption(id, "f","s"))}
  //  Await.ready(Http.server.serve(":8081", opt.toService))

  //  case class TestOption(id: Long, fisrt: Option[String], second: Option[String])
  //  val opt: Endpoint[TestOption] =
  //    get("test" :: path[Long]) { id: Long => Ok(TestOption(id, Some("f"),Some("s")))}
  //  Await.ready(Http.server.serve(":8081", opt.toService))

  //  case class TestOption(id: Long, first: Option[String], second: Option[Date], third: Option[Boolean])
  //  val opt: Endpoint[TestOption] =
  //    get("test" :: path[Long]) { id: Long => Ok(TestOption(id, Some("f"), Some(new Date()), Some(true)))}
  //  Await.ready(Http.server.serve(":8081", opt.toService))

  //  case class TestOption(id: Long, first: Option[String], second: Option[Date], third: Option[Boolean])
  //  val opt: Endpoint[Option[TestOption]] =
  //    get("test" :: path[Long]) { id: Long => Ok(Option(TestOption(id, Some("f"), Some(new Date()), Some(true))))}
  //  Await.ready(Http.server.serve(":8081", opt.toService))


  val ur = UserRow(
    id = None,
    firstname = Some("TestFN"),
    lastname = Some("TestLN"),
    login = Some("TestLogin"),
    email = Some("TestEMAIL"),
    gender = Some(true), // false - fm , true - m
    description = None,
    isactive = true,
    //    createdat = Some(new Date()),
    createdat = Some(new Timestamp(new Date().getTime)),
    updatedat = None,
    deletedat = None,
    isdeleted = false
  )

  val user: Endpoint[Option[UserRow]] = get("user" :: path[Long]) { id: Long => Ok(Option(ur)) }
  Await.ready(Http.server.serve(TypesafeConfig.interface + TypesafeConfig.port, user.toService))


  //  // finch
  //  case class Locale(language: String, country: String)
  //
  //  case class Time(locale: Locale, time: String)
  //
  //  def currentTime(l: java.util.Locale): String =
  //    java.util.Calendar.getInstance(l).getTime.toString
  //
  //  val time: Endpoint[Time] =
  //    post("time" :: jsonBody[Locale]) { l: Locale =>
  //      Ok(Time(l, currentTime(new java.util.Locale(l.language, l.country))))
  //    }
  ////
  //  Await.ready(Http.server.serve(":8081", time.toService))

  //  // static
  //  val api: Endpoint[String] = get("hello") { Ok("Hello, World!") }
}

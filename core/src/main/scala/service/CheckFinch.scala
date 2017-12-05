package service

import java.util.Date

import com.anymessenger.db.projection.UserRow
import com.twitter.finagle.Http
import com.twitter.util.Await
import io.circe.generic.auto._
import io.finch._
import io.circe._
//import io.finch.circe._
//import io.circe.generic.semiauto._
import io.finch.circe.dropNullValues._


object CheckFinch extends App {
  implicit val dateEncoder: Encoder[Date] = Encoder.instance(a => Json.fromLong(a.getTime))
  implicit val dateDecoder: Decoder[Date] = Decoder.instance(a => a.as[Long].map(new Date(_)))

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

  val user: Endpoint[Option[UserRow]] = get("user" :: path[Long]) { id: Long => Ok(Option(ur)) }
  Await.ready(Http.server.serve(":8081", user.toService))




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

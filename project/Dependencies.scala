import sbt._

//@formatter:off
object Dependencies {

  lazy val versions = Map(
    "cats" -> "1.0.0-RC1",
    "shapeless" -> "2.3.3",
    "slick" -> "3.2.1",
    "circe" -> "0.9.0-M2",
    "finch" -> "0.16.0-M5",
    "http4sVersion" -> "0.18.0-M8",
    "test" -> "3.0.1",

    // logging
    "logback" -> "1.2.3",
    "scalalogging" -> "3.7.2",

    // no uses
    "monocle" -> "1.4.0",
    "akka" -> "2.5.6",
    "akka-http" -> "10.0.10",
    "log4j" -> "2.10.0",
  )
  //--------------------------------------------------------------------------------------------------------------------

  val commonDeps: Seq[ModuleID] = Seq(
    "com.chuusai" %% "shapeless" % versions("shapeless"),
    "org.typelevel" %% "cats-core" % versions("cats"),

    // json
    "io.circe" %% "circe-core" % versions("circe"),
    "io.circe" %% "circe-generic" % versions("circe"),
    "io.circe" %% "circe-parser" % versions("circe"),
    "io.circe" %% "circe-literal" % versions("circe"),

    //"org.json4s" %% "json4s-jackson" % "3.5.3",
    //"org.json4s" %% "json4s-native" % "3.5.3",

    // logs
    "ch.qos.logback" % "logback-classic" % versions("logback"),
    "com.typesafe.scala-logging" %% "scala-logging" % versions("scalalogging"),
    //    "org.apache.logging.log4j" % "log4j-core" % versions("log4j"),
    //    "org.apache.logging.log4j" % "log4j-api" % versions("log4j"),
    //    "org.apache.logging.log4j" % "log4j-slf4j-impl" % versions("log4j"),
    //    "com.typesafe.scala-logging" %% "scala-logging" % "3.+",

    // common
    //"org.scala-lang.modules" %% "scala-xml" % "1.0.6",
    //"commons-lang" % "commons-lang" % "2.6",
    //"javax.mail" % "mail" % "1.4.7",
    //"joda-time" % "joda-time" % "2.9.9",
    //compilerPlugin("org.scalamacros" %% "paradise" % "2.1.0" cross CrossVersion.full)
  )

  //--------------------------------------------------------------------------------------------------------------------
  val jdbcDeps: Seq[ModuleID] =
    Seq(
      "com.typesafe.slick" %% "slick" % versions("slick"),
      "com.typesafe.slick" %% "slick-hikaricp" % versions("slick"),
      "io.underscore" %% "slickless" % "0.3.2",
      "org.postgresql" % "postgresql" % "42.1.4",
      "com.h2database" % "h2" % "1.4.196",
    )

  val http4sDeps: Seq[ModuleID] =
    Seq(
      "org.http4s" %% "http4s-dsl" % versions("http4sVersion"),
      "org.http4s" %% "http4s-blaze-server" % versions("http4sVersion"),
      "org.http4s" %% "http4s-blaze-client" % versions("http4sVersion"),
      "org.http4s" %% "http4s-circe" % versions("http4sVersion"),
    )
  val finagleDeps: Seq[ModuleID] =
    Seq(
      "com.github.finagle" %% "finch-core" % "0.16.0-M5",
      "com.github.finagle" %% "finch-circe" % "0.16.0-M5",
      "io.circe" %% "circe-generic" % "0.9.0-M2"
    )

  //--------------------------------------------------------------------------------------------------------------------

  val akkaHttpDeps: Seq[ModuleID] =
    Seq(
      // http
      "com.typesafe.akka" %% "akka-http-core" % versions("akka-http"),
      "com.typesafe.akka" %% "akka-http" % versions("akka-http"),
      "com.typesafe.akka" %% "akka-http-jackson" % versions("akka-http"),
      "com.typesafe.akka" %% "akka-http-spray-json" % versions("akka-http"),

      // test
      "com.typesafe.akka" %% "akka-http-testkit" % versions("akka-http") % "test",

      // docs
      // "com.github.swagger-akka-http" %% "swagger-akka-http" % "0.11.0"
    )

  val akkaDeps: Seq[ModuleID] =
    Seq(
      "com.typesafe.akka" %% "akka-actor" % versions("akka"),
      "com.typesafe.akka" %% "akka-stream" % versions("akka"),
      "com.typesafe.akka" %% "akka-contrib" % versions("akka"),
      "com.typesafe.akka" %% "akka-slf4j" % versions("akka"),
      // test scope
      "com.typesafe.akka" %% "akka-testkit" % versions("akka") % "test"
    )

  //--------------------------------------------------------------------------------------------------------------------

  val monocleDeps: Seq[ModuleID] =
    Seq(
      "com.github.julien-truffaut" %% "monocle-core" % versions("monocle"),
      "com.github.julien-truffaut" %% "monocle-generic" % versions("monocle"),
      "com.github.julien-truffaut" %% "monocle-macro" % versions("monocle"),
      "com.github.julien-truffaut" %% "monocle-state" % versions("monocle"),
      "com.github.julien-truffaut" %% "monocle-refined" % versions("monocle"),
      "com.github.julien-truffaut" %% "monocle-law" % versions("monocle") % "test"
    )

  //------------------------------------------------------------------------------------------------------------

  val testDeps: Seq[ModuleID] =
    Seq(
      "org.scalactic" %% "scalactic" % versions("test"),
      "org.scalatest" %% "scalatest" % versions("test") % "test",
    )

  //------------------------------------------------------------------------------------------------------------

}

// /@formatter:on

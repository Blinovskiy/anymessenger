import BuildSettings._
import Dependencies._
import sbt.Keys._

scalaVersion in ThisBuild := "2.12.4"


lazy val `core` = (project in file("core"))
  .dependsOn(`db-api`)
  .settings(build(commonDeps ++ http4sDeps ++ finagleDeps ++ jdbcDeps ++ testDeps): _*)
  .settings(
    name := "anymessenger",
    organization := "com.anymessenger",
    version := "0.1",
    //mainClass in Compile := Some("service.MainService"),
    mainClass in assembly := Some("service.MainService"),
    assemblyJarName in assembly := s"anymessenger-${version.value}.jar",

    // revolver
    Revolver.settings,
    javaOptions in reStart += "-Xmx2g",
    mainClass in reStart := Some("service.MainHttp4s"),
    reColors := Seq("blue", "green", "magenta"),
    //  Revolver.enableDebugging(port = 5050, suspend = true),
    //  envVars in reStart := Map("USER_TOKEN" -> "2359298356239")

    // scalajs-react
    libraryDependencies += "com.github.japgolly.scalajs-react" %%% "core" % versions("scalajsReact"),
    npmDependencies in Compile ++= Seq(
      "react" -> versions("react"),
      "react-dom" -> versions("react-dom")
    ),
  )
  .enablePlugins(ScalaJSPlugin)
  .enablePlugins(ScalaJSBundlerPlugin)

lazy val `common` = (project in file("common"))
  .settings(build(commonDeps ++ jdbcDeps ++ testDeps): _*)

lazy val `db-api` = (project in file("db-api"))
  .dependsOn(`common`)
  .settings(build(commonDeps ++ jdbcDeps): _*)

lazy val `devTools` = (project in file("devTools"))
  .dependsOn(`common`)
  .settings(build(Seq("com.typesafe.slick" %% "slick-codegen" % "3.2.0") ++ commonDeps ++ jdbcDeps): _*)


// dev
addCommandAlias("s", "; core/reStart")
addCommandAlias("st", "; core/reStop")

// build
addCommandAlias("pack", "; clean; core/assembly")



//// todo: use plugin for frontend building
//val buildFrontend = taskKey[Unit]("Execute frontend scripts")
//buildFrontend := {
//  import scala.language.postfixOps
//  import scala.sys.process._
//
//  val s: TaskStreams = streams.value
//  val shell: Seq[String] = Seq("bash", "-c")
//  val npmInstall: Seq[String] = shell :+ "cd js-ui && npm install"
//  val npmBuild: Seq[String] = shell :+ "cd js-ui && npm run build"
//  val copyBundle: Seq[String] = shell :+ "mkdir -p ./core/target/scala-2.12/classes/jsui && cp -rf ./js-ui/dist/* ./core/target/scala-2.12/classes/jsui"
//  s.log.info("Frontend build has been started...")
//
//  val packageTask: ProcessBuilder =
//    npmInstall #&&
//      npmBuild
//  //    #&& copyBundle
//
//  if ((packageTask !) == 0) {
//    s.log.success("Frontend has been built successfully!")
//  } else {
//    throw new IllegalStateException("frontend build failed!")
//  }
//}
//addCommandAlias("pf", "; clean; buildFrontend; core/assembly")


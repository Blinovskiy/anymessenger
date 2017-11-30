import BuildSettings._
import Dependencies._
import sbt.Keys._

scalaVersion in ThisBuild := "2.12.4"


lazy val `core` = (project in file("core"))
  .dependsOn(`db-api`)
  .settings(build(commonDeps ++ jdbcDeps ++ testDeps): _*) //akkaHttpDeps ++ akkaDeps ++
  .settings(
    name := "anymessenger",
    organization := "com.anymessenger",
    version := "0.1",
    mainClass in assembly := Some("com.anymessenger.Main"),
    assemblyJarName in assembly := s"anymessenger-${version.value}.jar"
  )

lazy val `common` = (project in file("common"))
  .settings(build(commonDeps ++ jdbcDeps ++ testDeps): _*) //akkaDeps ++ akkaHttpDeps ++

lazy val `db-api` = (project in file("db-api"))
  .dependsOn(`common`)
  .settings(build(commonDeps ++ jdbcDeps): _*)

lazy val `devTools` = (project in file("devTools"))
  .dependsOn(`common`)
  .settings(build(Seq("com.typesafe.slick" %% "slick-codegen" % "3.2.0") ++ commonDeps ++ jdbcDeps): _*)


// todo: use plugin for frontend building
val buildFrontend = taskKey[Unit]("Execute frontend scripts")
buildFrontend := {
  import scala.language.postfixOps
  import scala.sys.process._

  val s: TaskStreams = streams.value
  val shell: Seq[String] = Seq("bash", "-c")
  val npmInstall: Seq[String] = shell :+ "cd js-ui && npm install"
  val npmBuild: Seq[String] = shell :+ "cd js-ui && npm run build"
  val copyBundle: Seq[String] = shell :+ "mkdir -p ./core/target/scala-2.12/classes/jsui && cp -rf ./js-ui/dist/* ./core/target/scala-2.12/classes/jsui"
  s.log.info("Frontend build has been started...")

  val packageTask: ProcessBuilder =
    npmInstall #&&
      npmBuild
  //    #&& copyBundle

  if ((packageTask !) == 0) {
    s.log.success("Frontend has been built successfully!")
  } else {
    throw new IllegalStateException("frontend build failed!")
  }
}



addCommandAlias("m", "; clean; core/assembly")
addCommandAlias("p", "; clean; core/assembly")
//addCommandAlias("pf", "; clean; buildFrontend; core/assembly")
addCommandAlias("c", "; compile")
addCommandAlias("cc", ";clean ;compile")
addCommandAlias("cuc", ";clean ;update ;compile")
addCommandAlias("cucp", ";clean ;update ;compile ;package")








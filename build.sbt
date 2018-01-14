import Dependencies._
import sbt.Keys._

lazy val root = (project in file("."))
  .settings(commonSettings)
  .settings(revolverSettings)
  .settings(assemblySettings)
  .settings(
    name := "anymessenger",
    organization := "com.anymessenger",
    version := "0.1",
    mainClass in Compile := Some("com.anymessenger.server.http4s.Http4sServer"),
    libraryDependencies ++= commonDeps ++ http4sDeps ++ jdbcDeps ++ testDeps
  )
  .dependsOn(slick_model)
  .dependsOn(common)

lazy val common = (project in file("common"))
  .settings(commonSettings)
  .settings(libraryDependencies ++= commonDeps ++ jdbcDeps ++ testDeps)

lazy val slick_model = (project in file("slick_model"))
  .settings(commonSettings)
  .settings(libraryDependencies ++= commonDeps ++ jdbcDeps)
  .dependsOn(common)

lazy val devTools = (project in file("devTools"))
  .settings(commonSettings)
  .settings(generate := slickCodeGenTask.value)
  .settings(unmanagedBase := file("lib"))
  .settings(libraryDependencies ++= Seq("com.typesafe.slick" %% "slick-codegen" % "3.2.0") ++ commonDeps ++ jdbcDeps)
  .dependsOn(common)


lazy val revolverSettings = Revolver.settings ++ Seq(
  javaOptions in reStart += "-Xmx2g",
  mainClass in reStart := Some("com.anymessenger.server.http4s.Http4sServer"),
  reColors := Seq("blue", "green", "magenta"),
  fork in run := true,
  //  Revolver.enableDebugging(port = 5050, suspend = true),
  //  envVars in reStart := Map("USER_TOKEN" -> "2359298356239")
  javaOptions in run ++= Seq("-Ddatabase=postgres"),
  javaOptions in reStart ++= Seq("-Ddatabase=postgres")
)

lazy val assemblySettings = Seq(
  //mainClass in assembly := Some("com.anymessenger.server.http4s.Http4sServer"),
  assemblyJarName in assembly := s"anymessenger.jar",
  test in assembly := {}
)

lazy val commonSettings = Seq(
//  resolvers ++= Seq(Resolver.mavenLocal),

  resolvers += Resolver.sonatypeRepo("snapshots"),
  scalaVersion := "2.12.4",
  unmanagedBase := file("lib"),
  scalacOptions := Seq(
    "-deprecation",
    "-unchecked",
    "-encoding", "utf8",
    "-feature",
    "-language:postfixOps",
    "-language:implicitConversions",
    "-language:existentials",
    "-language:reflectiveCalls",
    "-language:higherKinds",
    "-Ypartial-unification"
  ),
)

// code generation task that calls the customized code generator
lazy val generate = taskKey[Seq[File]]("Generate Slick Tables")
lazy val slickCodeGenTask = Def.task {
  val dir = baseDirectory.all(rootFilter).value.head
  val cp = (fullClasspath in Compile).value.files //:+ (dir / "lib" / "postgresql-42.1.4.jar")
  val r = (runner in Compile).value
  val s = streams.value
  val outputDir = (dir / "slick_model" / "src" / "main" / "scala").getPath // place generated files in sbt's managed sources folder
  r.run("codegen.Generator", cp, Array(outputDir), s.log).failed foreach (sys error _.getMessage)
  val fname = outputDir + "/Tables.scala"
  Seq(file(fname))
}
lazy val rootFilter = ScopeFilter(inProjects(root))

// todo: use plugin for frontend building
val buildUI = taskKey[Unit]("Execute frontend scripts")
buildUI := {
  import scala.language.postfixOps
  import scala.sys.process._

  val s: TaskStreams = streams.value
  val shell: Seq[String] = Seq("bash", "-c")
  val npmInstall: Seq[String] = shell :+ "cd ui && npm install"
  val npmBuild: Seq[String] = shell :+ "cd ui && npm run-script build"
  val copyBundle: Seq[String] = shell :+ "mkdir -p ./target/scala-2.12/classes/ui/dist && cp -rf ./ui/dist/* ./target/scala-2.12/classes/ui"

  s.log.info("buildUI task is in progress...")

  val packageTask: ProcessBuilder =
    npmInstall #&&
      npmBuild #&&
      copyBundle

  if ((packageTask !) == 0) {
    s.log.success("buildUI task was finished successfully!")
  } else {
    throw new IllegalStateException("buildUI task failed!")
  }
}

// dev
addCommandAlias("s", "; reStart")
addCommandAlias("r", "; ~reStart")
addCommandAlias("st", "; reStop")

// dev with ui
addCommandAlias("sb", "; clean; buildUI; reStart")

// build with ui
addCommandAlias("pack", "; clean; buildUI; assembly")


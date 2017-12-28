import Dependencies._
import sbt.Keys._

lazy val root = (project in file("."))
  .settings(commonSettings)
  .settings(revolverSettings)
  .settings(
    name := "anymessenger",
    organization := "com.anymessenger",
    version := "0.1",
    mainClass in Compile := Some("com.anymessenger.server.finch.FinchServer"),
    //mainClass in Compile := Some("com.anymessenger.server.http4s.Http4sServer"),
    libraryDependencies ++= commonDeps ++ http4sDeps ++ finagleDeps ++ jdbcDeps ++ testDeps
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
  mainClass in reStart := Some("com.anymessenger.server.finch.FinchServer"),
  reColors := Seq("blue", "green", "magenta"),
  fork in run := true,
  //  Revolver.enableDebugging(port = 5050, suspend = true),
  //  envVars in reStart := Map("USER_TOKEN" -> "2359298356239")
  //  javaOptions in run ++= Seq("-Ddatabase=H2"),
  //  javaOptions in reStart ++= Seq("-Ddatabase=H2")
  javaOptions in run ++= Seq("-Ddatabase=postgres"),
  javaOptions in reStart ++= Seq("-Ddatabase=postgres")
)

lazy val assemblySettings = Seq(
  //mainClass in assembly := Some("com.anymessenger.server.finch.FinchServer"),
  assemblyJarName in assembly := s"anymessenger-${version.value}.jar",
  test in assembly := {}
)

lazy val commonSettings = Seq(
  resolvers ++= Seq(Resolver.mavenLocal),
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
  //    resolvers ++= Seq(
  //        "Sonatype snapshots" at "http://oss.sonatype.org/content/repositories/snapshots",
  //        "Sonatype releases" at "http://oss.sonatype.org/content/repositories/releases",
  //        "sonatype.repo" at "https://oss.sonatype.org/content/repositories/public/",
  //        "apache.repo" at "https://repository.apache.org/content/repositories/snapshots/",
  //        //      "Typesafe Releases" at "http://repo.typesafe.com/typesafe/maven-releases/",
  //        //      "confluent" at "http://packages.confluent.io/maven/"
  //    ),
  //    assemblyMergeStrategy in assembly := {
  //        case PathList("org", "slf4j", "impl", xs @ _*) => MergeStrategy.first // take from logback-classic-1.1.11
  //        case PathList("org", "slf4j", xs @ _*) => MergeStrategy.last // take from slf4j-api-1.7.22
  //        case "overview.html" => MergeStrategy.discard // remove overview.html from org.springframework libs
  //        case x =>
  //            val oldStrategy = (assemblyMergeStrategy in assembly).value
  //            oldStrategy(x)
  //    }
)

// code generation task that calls the customized code generator
lazy val generate = taskKey[Seq[File]]("Generate Slick Tables")
lazy val slickCodeGenTask = Def.task {
  val dir = baseDirectory.all(rootFilter).value.head
  val cp = (fullClasspath in Compile).value.files :+ (dir / "lib" / "postgresql-42.1.4.jar")
  val r = (runner in Compile).value
  val s = streams.value
  val outputDir = (dir / "slick_model" / "src" / "main" / "scala").getPath // place generated files in sbt's managed sources folder
  r.run("codegen.Generator", cp, Array(outputDir), s.log).failed foreach (sys error _.getMessage)
  val fname = outputDir + "/Tables.scala"
  Seq(file(fname))
}
lazy val rootFilter = ScopeFilter(inProjects(root))

// dev
addCommandAlias("s", "; reStart")
addCommandAlias("st", "; reStop")

// build
addCommandAlias("pack", "; clean; assembly")


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


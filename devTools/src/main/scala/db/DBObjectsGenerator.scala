package db

import com.typesafe.config.ConfigFactory
import slick.codegen.SourceCodeGenerator
import slick.jdbc.meta.MTable

import scala.concurrent.duration.Duration
import scala.concurrent.{Await, Future}

object DBObjectsGenerator extends App with OracleDbComponent {

  import driver.api._

  override val db: Database = Database.forConfig("oracle")

  import scala.concurrent.ExecutionContext.Implicits.global

  val dbConfig = ConfigFactory.load().getConfig("oracle")

  val dbObjectsToGenerate = List(
    "USER",
    "MESSAGE"
  )

  def schema = Option("ANY_DEV")

  val path = getClass.getResource("").getPath

  def gen(file: String, goal: String): Future[Unit] = {
    val q = MTable.getTables(None, None, None, Some(Seq(goal))).map(_.filter(t => dbObjectsToGenerate.contains(t.name.name)))

    val modelAction = driver.createModel(Some(q))
    val modelFuture = db.run(modelAction)
    modelFuture.map {
      model =>
        val gen = new SourceCodeGenerator(model)
        gen.writeToFile("", path, "db", file, s"$file.scala")
    }
  }

  val f = gen("Tables", "TABLE")
  //  gen("Views", "VIEW")
  //  gen("MViews", "MATERIALIZED VIEW")

  Await.ready(f.recover { case e => println(e.getMessage) }, Duration.Inf)

}

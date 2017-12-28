package config

import com.typesafe.scalalogging.LazyLogging

object Slicker extends LazyLogging {
  private val database =
    Option(System.getProperty("database"))
      .getOrElse({
        logger.warn("Database was set to default(postgres)")
        "postgres"
      })
  val db = slick.jdbc.JdbcBackend.Database.forConfig(s"$database.db", TypesafeConfig.config)
}

trait Slicker {
  def db = Slicker.db
}

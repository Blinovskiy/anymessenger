package codegen

import slick.jdbc.PostgresProfile

object Config {
  val url  = "jdbc:postgresql://localhost:5432/postgres"
  val user = "postgres"
  val pswd = "anympass"
  val profile = PostgresProfile
  val jdbcDriver = "org.postgresql.Driver"
}

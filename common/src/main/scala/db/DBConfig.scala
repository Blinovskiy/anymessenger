package db

import java.sql.Timestamp
import java.util.Date

import slick.basic.DatabaseConfig
import slick.jdbc.{JdbcBackend, JdbcProfile}
//import slick.lifted.CanBeQueryCondition
import scala.concurrent.duration._

// http://slick.lightbend.com/doc/3.2.1/database.html
trait DBConfig {

  lazy val config: DatabaseConfig[JdbcProfile] = DatabaseConfig.forConfig[JdbcProfile]("postgres")
  lazy val profile: JdbcProfile = config.profile
  lazy val db: JdbcBackend#DatabaseDef = config.db

  val futureWaitTimeout: FiniteDuration = 5 second

  import profile.api._

  def schema: Option[String] = None

  implicit def session: Session = db.createSession()

  implicit lazy val mappedDate: BaseColumnType[Date] =
    profile.MappedColumnType.base[Date, Timestamp](
      d => new Timestamp(d.getTime),
      ts => new Date(ts.getTime)
    )

  //  implicit lazy val mappedStringList: BaseColumnType[List[String]] =
  //    profile.MappedColumnType.base[List[String], String](
  //      t => {
  //        if (t.isEmpty)
  //          ""
  //        else
  //          t.map(_.trim).mkString(";")
  //      },
  //      {
  //        case "" => Nil
  //        case str => str.split(";").toList
  //      }
  //    )
  //
  //  implicit class Enumeration2BaseColumnType[E <: Enumeration](enum: E) {
  //    def toBaseColumnType: BaseColumnType[E#Value] =
  //      profile.MappedColumnType.base[E#Value, Int](e => e.id, i => enum(i))
  //  }
  //
  //  implicit class MaybeFilter[X, Y](query: slick.lifted.Query[X, Y, Seq]) {
  //    def optionalFilter[T, R: CanBeQueryCondition](data: Option[T])(f: T => X => R): slick.lifted.Query[X, Y, Seq] = {
  //      data.map(v => query.withFilter(f(v))) getOrElse query
  //    }
  //  }

}
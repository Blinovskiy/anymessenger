package com.anymessenger.model
// AUTO-GENERATED Slick data model
/** Stand-alone Slick data model for immediate use */
object Tables extends {
  val profile = slick.jdbc.PostgresProfile
} with Tables

/** Slick data model trait for extension, choice of backend or usage in the cake pattern. (Make sure to initialize this late.) */
trait Tables {
  val profile: slick.jdbc.JdbcProfile
  import profile.api._
  import slick.model.ForeignKeyAction
  // NOTE: GetResult mappers for plain SQL are only generated for tables where Slick knows how to map the types of all columns.
  import slick.jdbc.{GetResult => GR}

  /** DDL for all tables. Call .create to execute. */
  lazy val schema: profile.SchemaDescription = Message.schema ++ User.schema
  @deprecated("Use .schema instead of .ddl", "3.0")
  def ddl = schema

  /** Entity class storing rows of table Message
   *  @param id Database column id SqlType(bigserial), AutoInc, PrimaryKey
   *  @param text Database column text SqlType(varchar), Length(256,true), Default(None)
   *  @param userid Database column userid SqlType(int8), Default(None)
   *  @param createdat Database column createdat SqlType(timestamp), Default(None)
   *  @param updatedat Database column updatedat SqlType(timestamp), Default(None)
   *  @param deletedat Database column deletedat SqlType(timestamp), Default(None)
   *  @param isdeleted Database column isdeleted SqlType(bool), Default(false) */
  final case class MessageRow(id: Option[Long], text: Option[String] = None, userid: Option[Long] = None, createdat: Option[java.sql.Timestamp] = None, updatedat: Option[java.sql.Timestamp] = None, deletedat: Option[java.sql.Timestamp] = None, isdeleted: Boolean = false)
  /** GetResult implicit for fetching MessageRow objects using plain SQL queries */
  implicit def GetResultMessageRow(implicit e0: GR[Option[Long]], e1: GR[Option[String]], e2: GR[Option[java.sql.Timestamp]], e3: GR[Boolean]): GR[MessageRow] = GR{
    prs => import prs._
    MessageRow.tupled((<<[Option[Long]], <<?[String], <<?[Long], <<?[java.sql.Timestamp], <<?[java.sql.Timestamp], <<?[java.sql.Timestamp], <<[Boolean]))
  }
  /** Table description of table message. Objects of this class serve as prototypes for rows in queries. */
  class Message(_tableTag: Tag) extends Table[MessageRow](_tableTag, "message") {
    def * = (id, text, userid, createdat, updatedat, deletedat, isdeleted) <> (MessageRow.tupled, MessageRow.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (Rep.Some(id), text, userid, createdat, updatedat, deletedat, Rep.Some(isdeleted)).shaped.<>({r=>import r._; _1.map(_=> MessageRow.tupled((_1.get, _2, _3, _4, _5, _6, _7.get)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

    /** Database column id SqlType(bigserial), AutoInc, PrimaryKey */
    val id: Rep[Option[Long]] = column[Option[Long]]("id", O.AutoInc, O.PrimaryKey)
    /** Database column text SqlType(varchar), Length(256,true), Default(None) */
    val text: Rep[Option[String]] = column[Option[String]]("text", O.Length(256,varying=true), O.Default(None))
    /** Database column userid SqlType(int8), Default(None) */
    val userid: Rep[Option[Long]] = column[Option[Long]]("userid", O.Default(None))
    /** Database column createdat SqlType(timestamp), Default(None) */
    val createdat: Rep[Option[java.sql.Timestamp]] = column[Option[java.sql.Timestamp]]("createdat", O.Default(None))
    /** Database column updatedat SqlType(timestamp), Default(None) */
    val updatedat: Rep[Option[java.sql.Timestamp]] = column[Option[java.sql.Timestamp]]("updatedat", O.Default(None))
    /** Database column deletedat SqlType(timestamp), Default(None) */
    val deletedat: Rep[Option[java.sql.Timestamp]] = column[Option[java.sql.Timestamp]]("deletedat", O.Default(None))
    /** Database column isdeleted SqlType(bool), Default(false) */
    val isdeleted: Rep[Boolean] = column[Boolean]("isdeleted", O.Default(false))
  }
  /** Collection-like TableQuery object for table Message */
  lazy val Message = new TableQuery(tag => new Message(tag))

  /** Entity class storing rows of table User
   *  @param id Database column id SqlType(bigserial), AutoInc, PrimaryKey
   *  @param firstname Database column firstname SqlType(varchar), Length(32,true), Default(None)
   *  @param lastname Database column lastname SqlType(varchar), Length(32,true), Default(None)
   *  @param login Database column login SqlType(varchar), Length(32,true), Default(None)
   *  @param email Database column email SqlType(varchar), Length(32,true), Default(None)
   *  @param gender Database column gender SqlType(bool), Default(None)
   *  @param description Database column description SqlType(varchar), Length(256,true), Default(None)
   *  @param isactive Database column isactive SqlType(bool), Default(true)
   *  @param createdat Database column createdat SqlType(timestamp), Default(None)
   *  @param updatedat Database column updatedat SqlType(timestamp), Default(None)
   *  @param deletedat Database column deletedat SqlType(timestamp), Default(None)
   *  @param isdeleted Database column isdeleted SqlType(bool), Default(false) */
  final case class UserRow(id: Option[Long], firstname: Option[String] = None, lastname: Option[String] = None, login: Option[String] = None, email: Option[String] = None, gender: Option[Boolean] = None, description: Option[String] = None, isactive: Boolean = true, createdat: Option[java.sql.Timestamp] = None, updatedat: Option[java.sql.Timestamp] = None, deletedat: Option[java.sql.Timestamp] = None, isdeleted: Boolean = false)
  /** GetResult implicit for fetching UserRow objects using plain SQL queries */
  implicit def GetResultUserRow(implicit e0: GR[Option[Long]], e1: GR[Option[String]], e2: GR[Option[Boolean]], e3: GR[Boolean], e4: GR[Option[java.sql.Timestamp]]): GR[UserRow] = GR{
    prs => import prs._
    UserRow.tupled((<<[Option[Long]], <<?[String], <<?[String], <<?[String], <<?[String], <<?[Boolean], <<?[String], <<[Boolean], <<?[java.sql.Timestamp], <<?[java.sql.Timestamp], <<?[java.sql.Timestamp], <<[Boolean]))
  }
  /** Table description of table user. Objects of this class serve as prototypes for rows in queries. */
  class User(_tableTag: Tag) extends Table[UserRow](_tableTag, "user") {
    def * = (id, firstname, lastname, login, email, gender, description, isactive, createdat, updatedat, deletedat, isdeleted) <> (UserRow.tupled, UserRow.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (Rep.Some(id), firstname, lastname, login, email, gender, description, Rep.Some(isactive), createdat, updatedat, deletedat, Rep.Some(isdeleted)).shaped.<>({r=>import r._; _1.map(_=> UserRow.tupled((_1.get, _2, _3, _4, _5, _6, _7, _8.get, _9, _10, _11, _12.get)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

    /** Database column id SqlType(bigserial), AutoInc, PrimaryKey */
    val id: Rep[Option[Long]] = column[Option[Long]]("id", O.AutoInc, O.PrimaryKey)
    /** Database column firstname SqlType(varchar), Length(32,true), Default(None) */
    val firstname: Rep[Option[String]] = column[Option[String]]("firstname", O.Length(32,varying=true), O.Default(None))
    /** Database column lastname SqlType(varchar), Length(32,true), Default(None) */
    val lastname: Rep[Option[String]] = column[Option[String]]("lastname", O.Length(32,varying=true), O.Default(None))
    /** Database column login SqlType(varchar), Length(32,true), Default(None) */
    val login: Rep[Option[String]] = column[Option[String]]("login", O.Length(32,varying=true), O.Default(None))
    /** Database column email SqlType(varchar), Length(32,true), Default(None) */
    val email: Rep[Option[String]] = column[Option[String]]("email", O.Length(32,varying=true), O.Default(None))
    /** Database column gender SqlType(bool), Default(None) */
    val gender: Rep[Option[Boolean]] = column[Option[Boolean]]("gender", O.Default(None))
    /** Database column description SqlType(varchar), Length(256,true), Default(None) */
    val description: Rep[Option[String]] = column[Option[String]]("description", O.Length(256,varying=true), O.Default(None))
    /** Database column isactive SqlType(bool), Default(true) */
    val isactive: Rep[Boolean] = column[Boolean]("isactive", O.Default(true))
    /** Database column createdat SqlType(timestamp), Default(None) */
    val createdat: Rep[Option[java.sql.Timestamp]] = column[Option[java.sql.Timestamp]]("createdat", O.Default(None))
    /** Database column updatedat SqlType(timestamp), Default(None) */
    val updatedat: Rep[Option[java.sql.Timestamp]] = column[Option[java.sql.Timestamp]]("updatedat", O.Default(None))
    /** Database column deletedat SqlType(timestamp), Default(None) */
    val deletedat: Rep[Option[java.sql.Timestamp]] = column[Option[java.sql.Timestamp]]("deletedat", O.Default(None))
    /** Database column isdeleted SqlType(bool), Default(false) */
    val isdeleted: Rep[Boolean] = column[Boolean]("isdeleted", O.Default(false))
  }
  /** Collection-like TableQuery object for table User */
  lazy val User = new TableQuery(tag => new User(tag))
}

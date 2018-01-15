package com.anymessenger.dbmodel
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
  lazy val schema: profile.SchemaDescription = Message.schema ++ Userinfo.schema
  @deprecated("Use .schema instead of .ddl", "3.0")
  def ddl = schema

  /** Entity class storing rows of table Message
   *  @param id Database column id SqlType(bigserial), AutoInc
   *  @param text Database column text SqlType(varchar), Length(256,true), Default(None)
   *  @param userid Database column userid SqlType(int8), Default(None)
   *  @param createdat Database column createdat SqlType(timestamp), Default(None)
   *  @param updatedat Database column updatedat SqlType(timestamp), Default(None)
   *  @param deletedat Database column deletedat SqlType(timestamp), Default(None)
   *  @param isdeleted Database column isdeleted SqlType(bool), Default(false) */
  final case class MessageRow(id: Long, text: Option[String] = None, userid: Option[Long] = None, createdat: Option[java.sql.Timestamp] = None, updatedat: Option[java.sql.Timestamp] = None, deletedat: Option[java.sql.Timestamp] = None, isdeleted: Boolean = false)
  /** GetResult implicit for fetching MessageRow objects using plain SQL queries */
  implicit def GetResultMessageRow(implicit e0: GR[Long], e1: GR[Option[String]], e2: GR[Option[Long]], e3: GR[Option[java.sql.Timestamp]], e4: GR[Boolean]): GR[MessageRow] = GR{
    prs => import prs._
    MessageRow.tupled((<<[Long], <<?[String], <<?[Long], <<?[java.sql.Timestamp], <<?[java.sql.Timestamp], <<?[java.sql.Timestamp], <<[Boolean]))
  }
  /** Table description of table message. Objects of this class serve as prototypes for rows in queries. */
  class Message(_tableTag: Tag) extends Table[MessageRow](_tableTag, "message") {
    def * = (id, text, userid, createdat, updatedat, deletedat, isdeleted) <> (MessageRow.tupled, MessageRow.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (Rep.Some(id), text, userid, createdat, updatedat, deletedat, Rep.Some(isdeleted)).shaped.<>({r=>import r._; _1.map(_=> MessageRow.tupled((_1.get, _2, _3, _4, _5, _6, _7.get)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

    /** Database column id SqlType(bigserial), AutoInc */
    val id: Rep[Long] = column[Long]("id", O.AutoInc)
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

  /** Entity class storing rows of table Userinfo
   *  @param id Database column id SqlType(bigserial), AutoInc
   *  @param firstname Database column firstname SqlType(varchar), Length(32,true), Default(None)
   *  @param lastname Database column lastname SqlType(varchar), Length(32,true), Default(None)
   *  @param login Database column login SqlType(varchar), Length(32,true)
   *  @param email Database column email SqlType(varchar), Length(32,true)
   *  @param gender Database column gender SqlType(bool), Default(None)
   *  @param description Database column description SqlType(varchar), Length(256,true), Default(None)
   *  @param createdat Database column createdat SqlType(timestamp)
   *  @param updatedat Database column updatedat SqlType(timestamp), Default(None)
   *  @param isactive Database column isactive SqlType(bool), Default(true)
   *  @param deletedat Database column deletedat SqlType(timestamp), Default(None)
   *  @param isdeleted Database column isdeleted SqlType(bool), Default(false) */
  final case class UserinfoRow(id: Long, firstname: Option[String] = None, lastname: Option[String] = None, login: String, email: String, gender: Option[Boolean] = None, description: Option[String] = None, createdat: Option[java.sql.Timestamp], updatedat: Option[java.sql.Timestamp] = None, isactive: Boolean = true, deletedat: Option[java.sql.Timestamp] = None, isdeleted: Boolean = false)
  /** GetResult implicit for fetching UserinfoRow objects using plain SQL queries */
  implicit def GetResultUserinfoRow(implicit e0: GR[Long], e1: GR[Option[String]], e2: GR[String], e3: GR[Option[Boolean]], e4: GR[Option[java.sql.Timestamp]], e5: GR[Boolean]): GR[UserinfoRow] = GR{
    prs => import prs._
    UserinfoRow.tupled((<<[Long], <<?[String], <<?[String], <<[String], <<[String], <<?[Boolean], <<?[String], <<?[java.sql.Timestamp], <<?[java.sql.Timestamp], <<[Boolean], <<?[java.sql.Timestamp], <<[Boolean]))
  }
  /** Table description of table userinfo. Objects of this class serve as prototypes for rows in queries. */
  class Userinfo(_tableTag: Tag) extends Table[UserinfoRow](_tableTag, "userinfo") {
    def * = (id, firstname, lastname, login, email, gender, description, createdat, updatedat, isactive, deletedat, isdeleted) <> (UserinfoRow.tupled, UserinfoRow.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (Rep.Some(id), firstname, lastname, Rep.Some(login), Rep.Some(email), gender, description, createdat, updatedat, Rep.Some(isactive), deletedat, Rep.Some(isdeleted)).shaped.<>({r=>import r._; _1.map(_=> UserinfoRow.tupled((_1.get, _2, _3, _4.get, _5.get, _6, _7, _8, _9, _10.get, _11, _12.get)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

    /** Database column id SqlType(bigserial), AutoInc */
    val id: Rep[Long] = column[Long]("id", O.AutoInc)
    /** Database column firstname SqlType(varchar), Length(32,true), Default(None) */
    val firstname: Rep[Option[String]] = column[Option[String]]("firstname", O.Length(32,varying=true), O.Default(None))
    /** Database column lastname SqlType(varchar), Length(32,true), Default(None) */
    val lastname: Rep[Option[String]] = column[Option[String]]("lastname", O.Length(32,varying=true), O.Default(None))
    /** Database column login SqlType(varchar), Length(32,true) */
    val login: Rep[String] = column[String]("login", O.Length(32,varying=true))
    /** Database column email SqlType(varchar), Length(32,true) */
    val email: Rep[String] = column[String]("email", O.Length(32,varying=true))
    /** Database column gender SqlType(bool), Default(None) */
    val gender: Rep[Option[Boolean]] = column[Option[Boolean]]("gender", O.Default(None))
    /** Database column description SqlType(varchar), Length(256,true), Default(None) */
    val description: Rep[Option[String]] = column[Option[String]]("description", O.Length(256,varying=true), O.Default(None))
    /** Database column createdat SqlType(timestamp) */
    val createdat: Rep[Option[java.sql.Timestamp]] = column[Option[java.sql.Timestamp]]("createdat")
    /** Database column updatedat SqlType(timestamp), Default(None) */
    val updatedat: Rep[Option[java.sql.Timestamp]] = column[Option[java.sql.Timestamp]]("updatedat", O.Default(None))
    /** Database column isactive SqlType(bool), Default(true) */
    val isactive: Rep[Boolean] = column[Boolean]("isactive", O.Default(true))
    /** Database column deletedat SqlType(timestamp), Default(None) */
    val deletedat: Rep[Option[java.sql.Timestamp]] = column[Option[java.sql.Timestamp]]("deletedat", O.Default(None))
    /** Database column isdeleted SqlType(bool), Default(false) */
    val isdeleted: Rep[Boolean] = column[Boolean]("isdeleted", O.Default(false))
  }
  /** Collection-like TableQuery object for table Userinfo */
  lazy val Userinfo = new TableQuery(tag => new Userinfo(tag))
}

package com.anymessenger.db.table

import com.anymessenger.db.projection.UserRow
import db.{DBConfig, Repository}
import slick.lifted.ProvenShape

trait UserTableRepository extends Repository {
  this: DBConfig =>

  import profile.api._

  lazy val Users = new TableQuery(tag => new UserTable(tag, schema))

  class UserTable(_tableTag: Tag, _schema: Option[String])
    extends Table[UserRow](_tableTag, _schema, "user")
      with PrimaryKeyMixin[UserRow]
      with IsActiveMixin[UserRow]
      with CreatedAtMixin[UserRow]
      with UpdatedAtMixin[UserRow]
      with DeletedAtMixin[UserRow]
      with IsDeletedMixin[UserRow] {
    def * : ProvenShape[UserRow] = (id, firstName, lastName, login, email, gender, description, isActive, createdAt, updatedAt, deletedAt, isDeleted) <> (UserRow.tupled, UserRow.unapply)
    //    /** Maps whole row to an option. Useful for outer joins. */
    //    def ? = (Rep.Some(id), firstname, lastname, Rep.Some(login), Rep.Some(email), gender, description, createdat, updatedat, Rep.Some(isactive), deletedat, Rep.Some(isdeleted)).shaped.<>({r=>import r._; _1.map(_=> UserRow.tupled((_1.get, _2, _3, _4.get, _5.get, _6, _7, _8, _9, _10.get, _11, _12.get)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

    /** Database column FIRSTNAME SqlType(VARCHAR2), Length(32,true) */
    val firstName: Rep[Option[String]] = column[Option[String]]("firstname", O.Length(32, varying = true))
    /** Database column LASTNAME SqlType(VARCHAR2), Length(32,true) */
    val lastName: Rep[Option[String]] = column[Option[String]]("lastname", O.Length(32, varying = true))
    /** Database column LOGIN SqlType(VARCHAR2), Length(32,true) */
    val login: Rep[Option[String]] = column[Option[String]]("login", O.Length(32, varying = true))
    /** Database column EMAIL SqlType(VARCHAR2), Length(32,true) */
    val email: Rep[Option[String]] = column[Option[String]]("email", O.Length(32, varying = true))
    /** Database column GENDER SqlType(NUMBER) */
    val gender: Rep[Option[Boolean]] = column[Option[Boolean]]("gender")
    /** Database column DESCRIPTION SqlType(VARCHAR2), Length(256,true) */
    val description: Rep[Option[String]] = column[Option[String]]("description", O.Length(256, varying = true))
  }

}






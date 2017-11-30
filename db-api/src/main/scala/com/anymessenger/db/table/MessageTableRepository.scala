package com.anymessenger.db.table

import com.anymessenger.db.projection.MessageRow
import db.{DBConfig, Repository}
import slick.lifted.ProvenShape

trait MessageTableRepository extends Repository {
  this: DBConfig =>

  import profile.api._

  lazy val Messages = new TableQuery(tag => new MessageTable(tag, schema))

  class MessageTable(_tableTag: Tag, _schema: Option[String])
    extends Table[MessageRow](_tableTag, _schema, "message")
      with PrimaryKeyMixin[MessageRow]
      with CreatedAtMixin[MessageRow]
      with UpdatedAtMixin[MessageRow]
      with DeletedAtMixin[MessageRow]
      with IsDeletedMixin[MessageRow] {
    def * : ProvenShape[MessageRow]= (id, text, userId, createdAt, updatedAt, deletedAt, isDeleted) <> (MessageRow.tupled, MessageRow.unapply)
//    /** Maps whole row to an option. Useful for outer joins. */
    //    def ? = (Rep.Some(id), text, userid, createdat, updatedat, deletedat, Rep.Some(isdeleted)).shaped.<>({r=>import r._; _1.map(_=> MessageRow.tupled((_1.get, _2, _3, _4, _5, _6, _7.get)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

    //    /** Database column ID SqlType(NUMBER), PrimaryKey */
    //    val id: Rep[Long] = column[Long]("ID", O.PrimaryKey)
    /** Database column TEXT SqlType(VARCHAR2), Length(256,true) */
    val text: Rep[Option[String]] = column[Option[String]]("text", O.Length(256, varying = true))
    /** Database column USERID SqlType(NUMBER) */
    val userId: Rep[Option[Long]] = column[Option[Long]]("userid")
  }

}






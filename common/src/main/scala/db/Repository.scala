package db

import java.util.Date

trait Repository {
  this: DBConfig =>

  import profile.api._

  trait PrimaryKeyMixin[T] {
    this: Table[T] =>
    val id: Rep[Option[Long]] = column[Option[Long]]("id", O.PrimaryKey, O.AutoInc)
  }

  trait IsActiveMixin[T] {
    this: Table[T] =>
    val isActive: Rep[Boolean] = column[Boolean]("isactive", O.Default(true))
  }

  trait IsDeletedMixin[T] {
    this: Table[T] =>
    val isDeleted: Rep[Boolean] = column[Boolean]("isdeleted", O.Default(false))
  }

  trait DeletedAtMixin[T] {
    this: Table[T] =>
    val deletedAt: Rep[Option[Date]] = column[Option[Date]]("deletedat", O.Default(None))
  }

  trait CreatedAtMixin[T] {
    this: Table[T] =>
    val createdAt: Rep[Option[Date]] = column[Option[Date]]("createdat", O.Default(None))
  }

  trait UpdatedAtMixin[T] {
    this: Table[T] =>
    val updatedAt: Rep[Option[Date]] = column[Option[Date]]("updatedat", O.Default(None))
  }

}

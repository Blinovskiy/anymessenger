package com.anymessenger.db

import com.anymessenger.db.table.{MessageTableRepository, UserTableRepository}
import db.DBConfig

trait AnymessengerDBObjects
  extends UserTableRepository
    with MessageTableRepository {
  this: DBConfig =>
}

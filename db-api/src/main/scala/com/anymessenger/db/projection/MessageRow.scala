package com.anymessenger.db.projection

import java.util.Date

case class MessageRow(
                       id: Option[Long],
                       text: Option[String],
                       userId: Option[Long],
                       createdAt: Option[Date],
                       updatedAt: Option[Date],
                       deletedAt: Option[Date],
                       isDeleted: Boolean = false
                     )

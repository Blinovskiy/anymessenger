package com.anymessenger.db.projection

import java.util.Date

case class UserRow(
                    id: Option[Long],
                    firstName: Option[String],
                    lastName: Option[String],
                    login: Option[String],
                    email: Option[String],
                    gender: Option[Boolean],
                    description: Option[String],
                    isActive: Boolean = true,
                    createdAt: Option[Date],
                    updatedAt: Option[Date],
                    deletedAt: Option[Date],
                    isDeleted: Boolean = false
                  )

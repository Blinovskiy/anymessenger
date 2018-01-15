package com.anymessenger.service.model.response

case class ResponseUserEntity(
                               id: Long,
                               firstname: Option[String] = None,
                               lastname: Option[String] = None,
                               login: String,
                               email: String,
                               gender: Option[Boolean] = None,
                               description: Option[String] = None,
                               isactive: Boolean = true,
                               createdat: Option[java.sql.Timestamp] = None,
                               updatedat: Option[java.sql.Timestamp] = None
                             )


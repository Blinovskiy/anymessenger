package com.anymessenger.service.model.response

case class ResponseMessageEntity(id: Long,
                                 text: Option[String] = None,
                                 userinfo: ResponseUserEntity,
                                 createdat: Option[java.sql.Timestamp] = None,
                                 updatedat: Option[java.sql.Timestamp] = None)


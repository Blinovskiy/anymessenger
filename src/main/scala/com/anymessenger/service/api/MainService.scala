package com.anymessenger.service.api

import com.anymessenger.model.Tables._

import scala.concurrent.{ExecutionContext, Future}

trait MainService {

  def getUser(userid: Long)(implicit executor: ExecutionContext): Future[Either[String, Option[UserRow]]]

  def getMessage(messageId: Long)(implicit executor: ExecutionContext): Future[Either[String, Option[MessageRow]]]

  def getUserMessages(userid: Long)(implicit executor: ExecutionContext): Future[Either[String, List[MessageRow]]]

}

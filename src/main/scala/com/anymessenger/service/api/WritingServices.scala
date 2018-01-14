package com.anymessenger.service.api

import com.anymessenger.dbmodel.Tables._
import com.anymessenger.service.model.MessageEntity

import scala.concurrent.{ExecutionContext, Future}

trait WritingServices {

  def insertOrUpdateMessage(messageEntity: MessageEntity)(implicit executor: ExecutionContext): Future[Either[Throwable, Long]]

  def deleteMessage(id: Long)(implicit executor: ExecutionContext): Future[Either[Throwable, Long]]

  def insertOrUpdateUser(user: UserRow)(implicit executor: ExecutionContext): Future[Either[Throwable, Long]]

}

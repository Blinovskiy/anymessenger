package com.anymessenger.service.api

import java.sql.Timestamp

import com.anymessenger.service.model.response.{ResponseMessageEntity, ResponseUserEntity}

import scala.concurrent.{ExecutionContext, Future}

trait ReadingServices {

  def getUser(userid: Long)(implicit executor: ExecutionContext): Future[Either[Throwable, Option[ResponseUserEntity]]]

  def getMessage(messageId: Long)(implicit executor: ExecutionContext): Future[Either[Throwable, Option[ResponseMessageEntity]]]

  def getUserMessages(userid: Long)(implicit executor: ExecutionContext): Future[Either[Throwable, List[ResponseMessageEntity]]]

  def getAllMessages()(implicit executor: ExecutionContext): Future[Either[Throwable, List[ResponseMessageEntity]]]

  def getMessagesByDate(from: Timestamp, to: Timestamp)(implicit executor: ExecutionContext): Future[Either[Throwable, List[ResponseMessageEntity]]]

  def getLastNMessages(n: Int)(implicit executor: ExecutionContext): Future[Either[Throwable, List[ResponseMessageEntity]]]

}

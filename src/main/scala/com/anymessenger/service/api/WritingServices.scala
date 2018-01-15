package com.anymessenger.service.api

import com.anymessenger.service.model.request.{RequestMessageEntity, RequestUserEntity}

import scala.concurrent.{ExecutionContext, Future}

trait WritingServices {

  def insertOrUpdateMessage(messageEntity: RequestMessageEntity)(implicit executor: ExecutionContext): Future[Either[Throwable, Long]]

  def deleteMessage(id: Long)(implicit executor: ExecutionContext): Future[Either[Throwable, Long]]

  def insertOrUpdateUser(userEntity: RequestUserEntity)(implicit executor: ExecutionContext): Future[Either[Throwable, Long]]

  def deleteUser(id: Long)(implicit executor: ExecutionContext): Future[Either[Throwable, Long]]

}

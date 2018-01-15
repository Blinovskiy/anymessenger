package com.anymessenger.service.model.request

case class RequestMessageEntity(
                          id: Option[Long],
                          text: String,
                          userId: Long
                        )

package com.anymessenger.service.model

case class MessageEntity(
                          id: Option[Long],
                          text: String,
                          userId: Long
                        )

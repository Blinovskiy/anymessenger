package com.anymessenger.service.model

case class UserEntity(
                       id: Option[Long] = None,
                       firstname: Option[String] = None,
                       lastname: Option[String] = None,
                       login: Option[String] = None,
                       email: Option[String] = None,
                       gender: Option[Boolean] = None,
                       description: Option[String] = None,
                       isactive: Boolean = true,
                       isdeleted: Boolean = false
                     )

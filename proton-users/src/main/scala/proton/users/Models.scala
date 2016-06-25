package proton.users

import java.util.UUID

import slick.driver.PostgresDriver.api._

class Users(tag: Tag) extends Table[(UUID, String, Option[UUID])](tag, "USER") {
  def id = column[UUID]("ID", O.PrimaryKey)
  def name = column[String]("NAME")
  def organisationId = column[Option[UUID]]("ORG_ID")
  def * = (id, name, organisationId)
}

class Groups(tag: Tag) extends Table[(UUID, String)](tag, "GROUP") {
  def id = column[UUID]("ID", O.PrimaryKey)
  def name = column[String]("NAME")
  def * = (id, name)
}

class Organisations(tag: Tag) extends Table[(UUID, String)](tag, "ORGANISATION") {
  def id = column[UUID]("ID", O.PrimaryKey)
  def name = column[String]("NAME")
  def * = (id, name)
}

class Resources(tag: Tag) extends Table[(UUID, Int)](tag, "RESOURCE") {
  def id = column[UUID]("ID", O.PrimaryKey)
  def resType = column[Int]("TYPE")
  def * = (id, resType)
}
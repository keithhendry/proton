package proton.users

import java.util.UUID

import UsersPostgresDriver.api._

trait Models {
  case class User(id: UUID, name: String, organization: Option[UUID])
  class Users(tag: Tag) extends Table[User](tag, "USERS") {
    def id = column[UUID]("ID", O.PrimaryKey)
    def name = column[String]("NAME")
    def organizationId = column[Option[UUID]]("ORG_ID")
    def organization = foreignKey("ORG_FK", organizationId, organizationsQuery)(_.id.?,
      onUpdate = ForeignKeyAction.Restrict)
    def * = (id, name, organizationId) <>(User.tupled, User.unapply)
  }
  val usersQuery = TableQuery[Users]

  case class Group(id: UUID, name: String)
  class Groups(tag: Tag) extends Table[Group](tag, "GROUPS") {
    def id = column[UUID]("ID", O.PrimaryKey)
    def name = column[String]("NAME")
    def * = (id, name) <>(Group.tupled, Group.unapply)
  }
  val groupsQuery = TableQuery[Groups]

  class UsersToGroups(tag: Tag) extends Table[(UUID, UUID)](tag, "USERS_GROUPS") {
    def userId = column[UUID]("USER")
    def groupId = column[UUID]("GROUP")
    def * = (userId, groupId)
    def userFK = foreignKey("USERS_FK", userId, usersQuery)(_.id, onUpdate = ForeignKeyAction.Restrict)
    def groupFK = foreignKey("GROUPS_FK", groupId, groupsQuery)(_.id, onUpdate = ForeignKeyAction.Restrict)
  }
  val usersToGroupsQuery = TableQuery[UsersToGroups]

  case class Organization(id: UUID, name: String)
  class Organizations(tag: Tag) extends Table[Organization](tag, "ORGANIZATIONS") {
    def id = column[UUID]("ID", O.PrimaryKey)
    def name = column[String]("NAME")
    def * = (id, name) <>(Organization.tupled, Organization.unapply)
  }
  val organizationsQuery = TableQuery[Organizations]

  case class Resource(id: UUID, playerId: UUID, resType: UUID, props: Map[String, String])
  class Resources(tag: Tag) extends Table[Resource](tag, "RESOURCES") {
    def id = column[UUID]("ID", O.PrimaryKey)
    def playerId = column[UUID]("ID", O.PrimaryKey)
    def resType = column[UUID]("TYPE")
    def props = column[Map[String, String]]("PROPS_HSTORE")
    def * = (id, playerId, resType, props) <>(Resource.tupled, Resource.unapply)
  }
  val resourcesQuery = TableQuery[Resources]

}
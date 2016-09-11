package proton.users

import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import akka.http.scaladsl.server.Directives._

import scala.concurrent.ExecutionContext

class UsersService(ldap: Ldap)(implicit executionContext: ExecutionContext) extends SprayJsonSupport
  with UsersProtocol {
  import Models._

  val route = pathPrefix("users") {
    pathEndOrSingleSlash {
      complete(ldap.getUsers)
    }
  }
}

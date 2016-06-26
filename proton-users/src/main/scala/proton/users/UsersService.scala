package proton.users

import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import akka.http.scaladsl.server.Directives._

import UsersPostgresDriver.api._

import scala.concurrent.ExecutionContext

class UsersService(db: Database)(implicit executionContext: ExecutionContext) extends SprayJsonSupport
  with UsersProtocol {

  import Models._

  val route = pathPrefix("users") {
    pathEndOrSingleSlash {
      val all = db.run(usersQuery.result)

      onSuccess(all) { users =>
        complete(users)
      }
    }
  }
}

package proton.users

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import akka.http.scaladsl.model.StatusCodes._
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.{ExceptionHandler, Route}
import akka.stream.ActorMaterializer
import com.typesafe.config.Config
import com.typesafe.scalalogging.LazyLogging
import com.unboundid.ldap.sdk._
import scaldi.{Injectable, Module, TypesafeConfigInjector}
import spray.json.{CompactPrinter, JsonPrinter}

import scala.concurrent.ExecutionContext

class UsersProtonModule(config: Config) extends Module {
  bind[ExecutionContext] to scala.concurrent.ExecutionContext.Implicits.global
  bind[ActorSystem] to ActorSystem("ProtonUsers", config) destroyWith (_.terminate())
  bind[JsonPrinter] to CompactPrinter
  bind[UsersService] to injected[UsersService]

  val connection = new LDAPConnection(config.getString("proton.ldap.host"), config.getInt("proton.ldap.port"))
  val bindResult = connection.bind(config.getString("proton.ldap.bind-dn"), config.getString("proton.ldap.password"))
  val connectionPool = new LDAPConnectionPool(connection, 1, config.getInt("proton.ldap.max-connections"))
  bind[LDAPConnectionPool] to connectionPool
  bind[Ldap] to new Ldap(connectionPool, config.getString("proton.ldap.base-dn"))
}

object UsersProtonApp extends App with Injectable with LazyLogging with SprayJsonSupport with UsersProtocol {
  ProtonConfig.parse("users-dev.conf", args).foreach(c => {
    val config = c.config
    implicit val injector = TypesafeConfigInjector(config) :: new UsersProtonModule(config)
    implicit val executionContext = inject[ExecutionContext]
    implicit val system = inject[ActorSystem]
    implicit val materializer = ActorMaterializer()
    implicit val printer = inject[JsonPrinter]

    implicit val exceptionHandler = ExceptionHandler {
      case e: Exception =>
        logger.error("HTTP unhandled exception.", e)
        var message = "HTTP unhandled exception."
        if (e != null) {
          message = e.getMessage
        }
        complete(InternalServerError -> Message(message, UsersEvents.Unhandled))
    }

    def route: Route = inject[UsersService].route

    Http().bindAndHandle(route, config.getString("proton.ip"), config.getInt("proton.users.http.port"))
  })
}

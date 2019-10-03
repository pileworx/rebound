package io.pileworx.rebound.port.primary.rest

import akka.actor.ActorSystem
import akka.event.{LogSource, Logging}
import akka.http.scaladsl.model.StatusCodes._
import akka.http.scaladsl.model.{ContentTypes, HttpEntity}
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import akka.http.scaladsl.server.directives.RouteDirectives.complete
import io.pileworx.rebound.application.ReboundService
import io.pileworx.rebound.common.akka.AkkaImplicits
import io.pileworx.rebound.domain.command.DefineMockCmd
import io.pileworx.rebound.port.primary.rest.protocol.MockProtocol

class MockRoutes(service: ReboundService) extends AkkaImplicits with MockProtocol {
  implicit val logSourceType: LogSource[MockRoutes] = new LogSource[MockRoutes] {
    override def genString(a: MockRoutes) = "mock-route"
    override def genString(a: MockRoutes, s: ActorSystem) = s"mock-route,$s"
  }
  val log = Logging(system, this)

  val acceptMessage = """{"status":"ACCEPTED"}"""
  val successMessage = """{"status":"SUCCESS"}"""

  val routes: Route = path("mock") {
    delete {
      complete {
        service.clear()
        log.info("All Mocks were cleared.")
        HttpEntity(ContentTypes.`application/json`, successMessage)
      }
    } ~
    put {
      entity(as[DefineMockCmd]) { cmd =>
        service.add(cmd)
        complete(Accepted -> HttpEntity(ContentTypes.`application/json`, acceptMessage))
      }
    } ~
    get {
      complete(OK -> service.findAll())
    }
  }
}
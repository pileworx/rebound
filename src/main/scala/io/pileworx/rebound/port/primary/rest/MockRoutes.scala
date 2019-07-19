package io.pileworx.rebound.port.primary.rest

import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import akka.http.scaladsl.model.StatusCodes.{Accepted, BadRequest, InternalServerError}
import akka.http.scaladsl.model.{ContentTypes, HttpEntity}
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import akka.http.scaladsl.server.directives.RouteDirectives.complete
import io.pileworx.rebound.application.ReboundService
import io.pileworx.rebound.common.akka.AkkaImplicits
import io.pileworx.rebound.domain.command.DefineMockCmd
import spray.json.{DefaultJsonProtocol, RootJsonFormat}

class MockRoutes(service: ReboundService) extends AkkaImplicits with SprayJsonSupport with DefaultJsonProtocol {

  implicit val defineMockCmdFormat: RootJsonFormat[DefineMockCmd] = jsonFormat7(DefineMockCmd)

  val acceptMessage = """{"status":"ACCEPTED"}"""
  val successMessage = """{"status":"SUCCESS"}"""
  val badRequestMessage = """{"status":"BAD REQUEST", "message":"Unrecognized method in submitted mock"}"""
  val internalServerErrorMessage = """{"status":"INTERNAL SERVER ERROR", "message":"An unhandled error occurred during processing"}"""

  val routes: Route = path("mock") {
    delete {
      complete {
        service.clear()
        HttpEntity(ContentTypes.`application/json`, successMessage)
      }
    } ~
    put {
      entity(as[DefineMockCmd]) { cmd =>
        val resp = service.add(cmd)
        resp match {
          case Accepted => complete(resp -> HttpEntity(ContentTypes.`application/json`, acceptMessage))
          case BadRequest => complete(resp -> HttpEntity(ContentTypes.`application/json`, badRequestMessage))
          case _ => complete(InternalServerError -> HttpEntity(ContentTypes.`application/json`, internalServerErrorMessage))
        }
      }
    }
  }
}
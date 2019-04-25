package io.pileworx.rebound.routes

import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import akka.http.scaladsl.model.{ContentTypes, HttpEntity}
import akka.http.scaladsl.model.StatusCodes.{Accepted, BadRequest, InternalServerError}
import akka.http.scaladsl.server.Directives.{as, concat, delete, entity, put}
import akka.http.scaladsl.server.Route
import akka.http.scaladsl.server.directives.PathDirectives.path
import akka.http.scaladsl.server.directives.RouteDirectives.complete
import io.pileworx.rebound.AkkaImplicits
import io.pileworx.rebound.storage.{DefineMockCmd, MockData}
import spray.json.{DefaultJsonProtocol, RootJsonFormat}

object MockRoutes extends AkkaImplicits with SprayJsonSupport with DefaultJsonProtocol {

  implicit val defineMockCmdFormat: RootJsonFormat[DefineMockCmd] = jsonFormat6(DefineMockCmd)

  val ACCEPTED_MESSAGE = """{"status":"ACCEPTED"}"""
  val SUCCESS_MESSAGE = """{"status":"SUCCESS"}"""
  val BAD_REQUEST_MESSAGE = """{"status":"BAD REQUEST", "message":"Unrecognized method in submitted mock"}"""
  val INTERNAL_SERVER_ERROR_MESSAGE = """{"status":"INTERNAL SERVER ERROR", "message":"An unhandled error occurred during possessing"}"""

  val routes: Route = path("mock") {
    concat(
      put {
        entity(as[DefineMockCmd]) { cmd =>
          val resp = MockData.add(cmd)
          resp match {
            case Accepted => complete(resp -> HttpEntity(ContentTypes.`application/json`, ACCEPTED_MESSAGE))
            case BadRequest => complete(resp -> HttpEntity(ContentTypes.`application/json`, BAD_REQUEST_MESSAGE))
            case _ => complete(InternalServerError -> HttpEntity(ContentTypes.`application/json`, INTERNAL_SERVER_ERROR_MESSAGE))
          }
        }
      },
      delete {
        complete {
          MockData.reset()
          HttpEntity(ContentTypes.`application/json`, SUCCESS_MESSAGE)
        }
      }
    )
  }
}
package io.pileworx.rebound.port.primary.rest

import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import akka.http.scaladsl.model.StatusCodes._
import akka.http.scaladsl.model.{ContentTypes, HttpEntity}
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import akka.http.scaladsl.server.directives.RouteDirectives.complete
import io.pileworx.rebound.application.ReboundService
import io.pileworx.rebound.common.akka.AkkaImplicits
import io.pileworx.rebound.domain.command.{DefineMockCmd, DefineRequestCmd, DefineResponseCmd, When}
import io.pileworx.rebound.domain.mock.{Header, Method}
import spray.json._

class MockRoutes(service: ReboundService) extends AkkaImplicits with SprayJsonSupport with DefaultJsonProtocol {

  val headerApply: (String, String) => Header = Header.apply
  implicit val methodFormat: RootJsonFormat[Method] = new RootJsonFormat[Method] {
    def read(value: JsValue): Method = Method(value.asInstanceOf[JsString].value)
    def write(method: Method): JsValue = JsString(method.value)
  }
  implicit val headerFormat: RootJsonFormat[Header] = jsonFormat2(headerApply)
  implicit val responseFormat: RootJsonFormat[DefineResponseCmd] = jsonFormat4(DefineResponseCmd)
  implicit val defineRequestCmd: RootJsonFormat[DefineRequestCmd] = jsonFormat5(DefineRequestCmd)
  implicit val whenFormat: RootJsonFormat[When] = jsonFormat1(When)
  implicit val defineMockCmdFormat: RootJsonFormat[DefineMockCmd] = jsonFormat3(DefineMockCmd)

  val acceptMessage = """{"status":"ACCEPTED"}"""
  val successMessage = """{"status":"SUCCESS"}"""

  val routes: Route = path("mock") {
    delete {
      complete {
        service.clear()
        HttpEntity(ContentTypes.`application/json`, successMessage)
      }
    } ~
    put {
      entity(as[DefineMockCmd]) { cmd =>
        service.add(cmd)
        complete(Accepted -> HttpEntity(ContentTypes.`application/json`, acceptMessage))
      }
    }
  }
}
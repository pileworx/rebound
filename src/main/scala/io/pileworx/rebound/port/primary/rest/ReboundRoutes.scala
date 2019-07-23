package io.pileworx.rebound.port.primary.rest

import akka.http.scaladsl.model.StatusCodes._
import akka.http.scaladsl.model._
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.directives.RouteDirectives.complete
import akka.http.scaladsl.server.{Route, StandardRoute}
import io.pileworx.rebound.application.ReboundService
import io.pileworx.rebound.common.akka.AkkaImplicits
import io.pileworx.rebound.domain.mock.{MockId, Response}

class ReboundRoutes(service: ReboundService) extends AkkaImplicits {

  private val badRequest: StandardRoute = complete(BadRequest, """{"status":"BAD REQUEST", "message":"No mocked data found"}""")

  val routes: Route = pass(
    extractRequest { request =>
      request.method match {
        case HttpMethods.CONNECT | HttpMethods.HEAD | HttpMethods.TRACE => complete(MethodNotAllowed)
        case _ => respond(request).getOrElse(badRequest)
      }
    }
  )

  private def respond(request: HttpRequest): Option[StandardRoute] = {
    service.nextResponseById(MockId(request)) match {
      case Some(resp) => Some(complete(resp.status -> HttpEntity(getContentType(resp), resp.body.getOrElse(""))))
      case _ => None
    }
  }

  private def getContentType(resp: Response): ContentType.WithCharset = {
    resp.getHeader("Content-Type") match {
      case Some(contentType) =>
        val mtParts = contentType.value.split("/")
        MediaType.customWithOpenCharset(mtParts(0), mtParts(1)) withCharset HttpCharsets.`UTF-8`
      case None =>
        MediaTypes.`text/plain` withCharset HttpCharsets.`UTF-8`
    }
  }
}
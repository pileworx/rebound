package io.pileworx.rebound.port.primary.rest

import akka.http.scaladsl.model.StatusCodes._
import akka.http.scaladsl.model._
import akka.http.scaladsl.server.Directives.{get, _}
import akka.http.scaladsl.server.directives.PathDirectives.path
import akka.http.scaladsl.server.directives.RouteDirectives.complete
import akka.http.scaladsl.server.{Route, StandardRoute}
import io.pileworx.rebound.application.{MockQuery, ReboundDao, ReboundService}
import io.pileworx.rebound.common.akka.AkkaImplicits
import io.pileworx.rebound.domain.MockId
import io.pileworx.rebound.domain.command.DefineMockCmd

class ReboundRoutes(service: ReboundService) extends AkkaImplicits {

  private val badRequest: StandardRoute = complete(BadRequest, """{"status":"BAD REQUEST", "message":"No mocked data found"}""")

  val routes: Route = pass(
    extractRequest { request =>
      request.method match {
        case HttpMethods.CONNECT || HttpMethods.HEAD || HttpMethods.TRACE => complete(MethodNotAllowed)
        case _ => respond(request).getOrElse(badRequest)
      }
    }
  )

  private def respond(request: HttpRequest): Option[StandardRoute] = {
    service.findById(MockId(request)) match {
      case Some(mock) => Some(complete(mock.status -> HttpEntity(getContentType(mock), mock.response.getOrElse(""))))
      case _ => None
    }
  }

  private def getPath(rPath: Uri.Path, params: Map[String, String]): String = {
    def paramString(param: (String, String)): String = s"""${param._1}=${param._2}"""

    if (params.nonEmpty)
      s"${rPath.toString()}?${params.map(paramString).mkString("&")}"
    else
      rPath.toString()
  }

  private def getContentType(mock: DefineMockCmd): ContentType.WithCharset = {
    val mtParts = mock.contentType.split("/")
    val mt = MediaType.customWithOpenCharset(mtParts(0), mtParts(1))
    mt withCharset HttpCharsets.`UTF-8`
  }
}
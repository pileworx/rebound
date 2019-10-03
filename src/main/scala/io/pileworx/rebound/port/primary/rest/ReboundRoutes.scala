package io.pileworx.rebound.port.primary.rest

import java.util.UUID
import akka.actor.ActorSystem
import akka.http.scaladsl.model.StatusCodes._
import akka.http.scaladsl.model._
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.directives.PathDirectives.path
import akka.http.scaladsl.server.directives.RouteDirectives.complete
import akka.http.scaladsl.server.{Route, StandardRoute}
import io.pileworx.rebound.application.ReboundService
import io.pileworx.rebound.common.akka.AkkaImplicits
import io.pileworx.rebound.domain.mock.{MockId, Response}
import akka.event.{LogSource, Logging}
import io.pileworx.rebound.common.logging.formatter.{HttpRequestFormatter, HttpResponseFormatter}

class ReboundRoutes(service: ReboundService) extends AkkaImplicits {

  implicit val logSourceType: LogSource[ReboundRoutes] = new LogSource[ReboundRoutes] {
    override def genString(a: ReboundRoutes) = "rebound-route"
    override def genString(a: ReboundRoutes, s: ActorSystem) = s"rebound-route,$s"
  }
  val log = Logging(system, this)
  val requestFormatter = new HttpRequestFormatter
  val responseFormatter = new HttpResponseFormatter

  private val badRequest: StandardRoute = complete(BadRequest, """{"status":"BAD REQUEST", "message":"No mocked data found"}""")

  val routes: Route = extractRequest { request =>
    path(RemainingPath) { rPath =>
      head { complete(MethodNotAllowed) } ~
      get { respond(request, None).getOrElse(badRequest) } ~
      delete { respond(request, None).getOrElse(badRequest) } ~
      entity(as[String]) { body =>
        put { respond(request, Some(body)).getOrElse(badRequest) } ~
        post { respond(request, Some(body)).getOrElse(badRequest) } ~
        patch { respond(request, Some(body)).getOrElse(badRequest) }
      }
    }
  }

  private def respond(request: HttpRequest, entity: Option[String]): Option[StandardRoute] = {
    val requestId = UUID.randomUUID().toString
    log.info("\nReceived Request: ID {} {}", requestId, requestFormatter.format(request, entity))
    service.nextResponseById(MockId(request, entity)) match {
      case Some(resp) =>
        log.info("\nReturned Response: ID {} {}", requestId, responseFormatter.format(resp))
        Some(complete(resp.status -> HttpEntity(getContentType(resp), resp.body.getOrElse(""))))
      case _ =>
        log.info("\nReturned Response: ID {} No Response Found.", requestId)
        None
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
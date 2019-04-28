package io.pileworx.rebound.port.primary.rest

import akka.http.scaladsl.model.StatusCodes._
import akka.http.scaladsl.model._
import akka.http.scaladsl.server.Directives.{get, _}
import akka.http.scaladsl.server.directives.PathDirectives.path
import akka.http.scaladsl.server.directives.RouteDirectives.complete
import akka.http.scaladsl.server.{Route, StandardRoute}
import io.pileworx.rebound.application.{DefineMockCmd, MockQuery, ReboundDao, ReboundService}
import io.pileworx.rebound.common.akka.AkkaImplicits

class ReboundRoutes(service: ReboundService) extends AkkaImplicits {

  private val badRequest: StandardRoute = complete(BadRequest, """{"status":"BAD REQUEST", "message":"No mocked data found"}""")

  val routes: Route = path(RemainingPath) { rPath =>
    parameterMap { params: Map[String, String] =>
      val key: String = getPath(rPath, params)
      concat(
        get { respond(MockQuery(ReboundDao.GET, key)).getOrElse(badRequest) },
        put { respond(MockQuery(ReboundDao.PUT, key)).getOrElse(badRequest) },
        post { respond(MockQuery(ReboundDao.POST, key)).getOrElse(badRequest) },
        head { complete(MethodNotAllowed) },
        patch { respond(MockQuery(ReboundDao.PATCH, key)).getOrElse(badRequest) },
        delete { respond(MockQuery(ReboundDao.DELETE, key)).getOrElse(badRequest) },
        options { complete(MethodNotAllowed) }
      )
    }
  }

  private def respond(query: MockQuery): Option[StandardRoute] = service.find(query) match {
    case Some(mock) => Some(complete(mock.status -> HttpEntity(getContentType(mock), mock.response.getOrElse(""))))
    case _ => None
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
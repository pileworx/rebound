package io.pileworx.rebound.routes

import akka.http.scaladsl.model.StatusCodes._
import akka.http.scaladsl.model.{ContentType, HttpCharsets, HttpEntity, MediaType, Uri}
import akka.http.scaladsl.server.Directives.{get, _}
import akka.http.scaladsl.server.{Route, StandardRoute}
import akka.http.scaladsl.server.directives.PathDirectives.path
import akka.http.scaladsl.server.directives.RouteDirectives.complete
import io.pileworx.rebound.AkkaImplicits
import io.pileworx.rebound.storage.DefineMockCmd
import io.pileworx.rebound.storage.MockData._

object ReboundRoutes extends AkkaImplicits {

  private val badRequest: StandardRoute = complete(BadRequest, """{"status":"BAD REQUEST", "message":"No mocked data found"}""")

  val routes: Route = path(RemainingPath) { rPath =>
    parameterMap { params: Map[String, String] =>
      val key: String = getPath(rPath, params)
      concat(
        get { respond(GET, key).getOrElse(badRequest) },
        put { respond(PUT, key).getOrElse(badRequest) },
        post { respond(POST, key).getOrElse(badRequest) },
        head { complete(MethodNotAllowed) },
        patch { respond(PATCH, key).getOrElse(badRequest) },
        delete { respond(DELETE, key).getOrElse(badRequest) },
        options { complete(MethodNotAllowed) }
      )
    }
  }

  private def respond(v: String, k:String): Option[StandardRoute] = getData(v,k) match {
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
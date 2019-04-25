package io.pileworx.rebound.routes

import akka.http.scaladsl.model.StatusCodes.BadRequest
import akka.http.scaladsl.model.{HttpCharsets, HttpEntity, MediaType}
import akka.http.scaladsl.server.Directives.{get, _}
import akka.http.scaladsl.server.Route
import akka.http.scaladsl.server.directives.PathDirectives.path
import akka.http.scaladsl.server.directives.RouteDirectives.complete
import io.pileworx.rebound.AkkaImplicits
import io.pileworx.rebound.storage.MockData._

object ReboundRoutes extends AkkaImplicits {
  val routes: Route = path(RemainingPath) { rPath =>
    parameterMap { params: Map[String, String] =>
      def paramString(param: (String, String)): String = s"""${param._1}=${param._2}"""
      val paramSt = s"${params.map(paramString).mkString("&")}"
      //TODO sort out prefix ? for query string
      get {
        val sRPath = s"${rPath.toString()}?$paramSt"
        if (verb(GET).contains(sRPath)) {
          val mock = verb(GET)(sRPath)
          val mtParts = mock.contentType.split("/")
          val mt = MediaType.customWithOpenCharset(mtParts(0), mtParts(1))
          val ct = mt withCharset HttpCharsets.`UTF-8`
          complete(mock.status -> HttpEntity(ct, mock.response))
        } else {
          complete(BadRequest, """{"status":"BAD REQUEST", "message":"No mocked data found"}""")
        }
      }
    }
  }
}
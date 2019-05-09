package io.pileworx.rebound.common.akka.http

import akka.http.scaladsl.model._
import akka.http.scaladsl.server._
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.model.headers._
import akka.http.scaladsl.model.HttpMethods._

trait Cors {
  private val corsResponseHeaders = List(
    `Access-Control-Allow-Origin`.*,
    `Access-Control-Allow-Credentials`(true),
    `Access-Control-Max-Age`(3600),
    `Access-Control-Allow-Headers`("Origin", "x-requested-with", "Content-Type", "Accept", "Authorization", "Allow")
  )

  private def addAccessControlHeaders: Directive0 = {
    respondWithHeaders(corsResponseHeaders)
  }

  private def preFlightRequestHandler: Route = options {
    complete(HttpResponse(StatusCodes.OK).
      withHeaders(`Access-Control-Allow-Methods`(HEAD, OPTIONS, POST, PUT, GET, DELETE, PATCH)))
  }

  def cors(r: Route): Route = addAccessControlHeaders {
    preFlightRequestHandler ~ r
  }
}

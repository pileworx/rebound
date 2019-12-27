package io.pileworx.rebound.common.akka.http

import akka.http.scaladsl.model._
import akka.http.scaladsl.model.headers.HttpOriginRange.Default
import akka.http.scaladsl.server._
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.model.headers._
import com.typesafe.config.{Config, ConfigFactory}

import collection.JavaConverters._
import scala.collection.immutable

trait Cors {
  private val corsResponseHeaders = List(
    Cors.allowedOrigins(),
    Cors.allowCredentials(),
    Cors.maxAge(),
    Cors.allowedHeaders()
  )
  private val accessControlHeaders: Directive0 = respondWithHeaders(corsResponseHeaders)

  def cors(r: Route): Route = accessControlHeaders {
    preFlightRequestHandler ~ r
  }
  private def preFlightRequestHandler: Route = options {
    complete(HttpResponse(StatusCodes.OK).
      withHeaders(Cors.allowedMethod()))
  }
}

object Cors {
  val config: Config = ConfigFactory.load()

  def allowedOrigins(): `Access-Control-Allow-Origin` = {
    val origins = config.getStringList("cors.allowed-origins").asScala
    if (origins.isEmpty || origins.head == "*")
      `Access-Control-Allow-Origin`.*
    else
      `Access-Control-Allow-Origin`(Default(immutable.Seq(origins.map(o => HttpOrigin(o)): _*)))
  }

  def allowCredentials(): `Access-Control-Allow-Credentials` = {
    `Access-Control-Allow-Credentials`(config.getBoolean("cors.allow-credentials"))
  }

  def allowedMethod(): `Access-Control-Allow-Methods` = {
    `Access-Control-Allow-Methods`(immutable.Seq(config.getStringList("cors.allowed-methods").asScala.map(m => HttpMethods.getForKey(m).get): _*))
  }

  def maxAge(): `Access-Control-Max-Age` = {
    `Access-Control-Max-Age`(config.getLong("cors.max-age"))
  }
  def allowedHeaders(): `Access-Control-Allow-Headers` = {
    `Access-Control-Allow-Headers`(immutable.Seq(config.getStringList("cors.allowed-headers").asScala: _*))
  }
}
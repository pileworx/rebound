package io.pileworx.rebound.common.akka.http

import akka.http.scaladsl.model._
import akka.http.scaladsl.model.headers.HttpOriginRange.Default
import akka.http.scaladsl.server._
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.model.headers._
import akka.http.scaladsl.model.HttpMethods._
import com.typesafe.config.{Config, ConfigException, ConfigFactory}

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
    try {
      val origins = config.getStringList("cors.allowed-origins").asScala
      `Access-Control-Allow-Origin`(Default(immutable.Seq(origins.map(o => HttpOrigin(o)): _*)))
    } catch {
      case _: ConfigException => `Access-Control-Allow-Origin`.*
    }
  }

  def allowCredentials(): `Access-Control-Allow-Credentials` = {
    try {
      `Access-Control-Allow-Credentials`(config.getBoolean("cors.allow-credentials"))
    } catch {
      case _: ConfigException => `Access-Control-Allow-Credentials`(true)
    }
  }

  def allowedMethod(): `Access-Control-Allow-Methods` = {
    try {
      `Access-Control-Allow-Methods`(immutable.Seq(config.getStringList("cors.allowed-methods").asScala.map(m => HttpMethods.getForKey(m).get): _*))
    } catch {
      case _: ConfigException => `Access-Control-Allow-Methods`(POST, GET, OPTIONS, DELETE, HEAD, PUT, PATCH)
    }
  }

  def maxAge(): `Access-Control-Max-Age` = {
    try {
      `Access-Control-Max-Age`(config.getLong("cors.max-age"))
    } catch {
      case _: ConfigException => `Access-Control-Max-Age`(3600)
    }
  }
  def allowedHeaders(): `Access-Control-Allow-Headers` = {
    try {
      `Access-Control-Allow-Headers`(immutable.Seq(config.getStringList("cors.allowed-headers").asScala: _*))
    } catch {
      case _: ConfigException => `Access-Control-Allow-Headers`("Origin", "x-requested-with", "Content-Type", "Accept", "Authorization", "Allow")
    }
  }
}
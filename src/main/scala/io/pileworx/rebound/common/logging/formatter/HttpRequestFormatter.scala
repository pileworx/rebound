package io.pileworx.rebound.common.logging.formatter

import akka.http.scaladsl.model.HttpRequest
import com.google.gson.JsonParser

class HttpRequestFormatter {

  private val jsonParser = new JsonParser

  def format(req: HttpRequest, entity: Option[String]): String = {
    val body = entity match {
      case Some(b) => jsonParser.parse(b).toString
      case None => ""
    }
    s"""
      |  Method: ${req.method.value}
      |  URI: ${req.uri}
      |  Headers:
      |${req.headers.map(h => s"    ${h.name().toLowerCase()}: ${h.value()}").mkString("\n")}
      |  Body: $body
      |""".stripMargin
  }
}

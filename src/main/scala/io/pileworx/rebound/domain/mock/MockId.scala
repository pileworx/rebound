package io.pileworx.rebound.domain.mock

import akka.http.scaladsl.model.HttpRequest
import com.google.gson.JsonParser
import io.pileworx.rebound.domain.command.DefineMockCmd

case class MockId(method: Method,
                  path: String,
                  query: Option[String],
                  headers: Option[Seq[Header]],
                  body: Option[String])

object MockId {
  private val jsonParser = new JsonParser

  def apply(cmd: DefineMockCmd): MockId = {
    val req = cmd.when.request
    val headers = req.headers match {
      case Some(rh) => Some(rh
        .filter(h => Header.request.contains(h.name.toLowerCase))
        .map(h => Header(h.name.toLowerCase, h.value)))
      case _ => None
    }

    MockId(req.method, req.path, req.query, headers, formatBody(req.body))
  }
  def apply(request: HttpRequest, entity: Option[String]): MockId = {
    val headers = if(request.headers.nonEmpty)
      Some(request.headers
        .filter(rh => Header.request.contains(rh.lowercaseName()))
        .map(rh => Header(rh.lowercaseName(), rh.value())))
    else
      None

    MockId(
      Method(request.method.value),
      request.uri.path.toString(),
      request.uri.rawQueryString,
      headers,
      formatBody(entity))
  }

  private def formatBody(body: Option[String]): Option[String] = body match {
    case Some(b) => Some(jsonParser.parse(b).toString)
    case _ => None
  }
}

package io.pileworx.rebound.domain.mock

import akka.http.scaladsl.model.HttpRequest
import io.pileworx.rebound.domain.command.DefineMockCmd

case class MockId(method: Method,
                  path: String,
                  query: Option[String],
                  headers: Option[Seq[Header]],
                  body: Option[String])

object MockId {
  def apply(cmd: DefineMockCmd): MockId = {
    val req = cmd.when.request
    new MockId(req.method, req.path, req.query, req.headers, req.body)
  }

  def apply(request: HttpRequest): MockId = {
    val headers = if(request.headers.nonEmpty)
      Some(request.headers.map(h => Header(h.name(), h.value())))
    else
      None

    val rawBody = request.entity.toString
    val body = if(rawBody.nonEmpty) Some(rawBody) else None

    new MockId(
      Method(request.method.value),
      request.uri.path.toString(),
      request.uri.rawQueryString,
      headers,
      body)
  }
}

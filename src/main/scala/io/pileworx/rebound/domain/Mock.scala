package io.pileworx.rebound.domain

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

case class Mock(id: MockId,
                scenario: String,
                responses: Option[Seq[Response]],
                executed: Boolean = false)

case class Header(name: String, value: String)

case class Response(status: Int,
                    headers: Option[Seq[Header]],
                    body: Option[String],
                    values: Option[Map[String, Any]],
                    used: Option[Boolean])

trait Method {
  protected val m: String
  def value: String = m
}

object Method {

  val GET = "GET"
  val PUT = "PUT"
  val POST = "POST"
  val HEAD = "HEAD"
  val PATCH = "PATCH"
  val DELETE = "DELETE"
  val OPTIONS = "OPTIONS"

  def apply(m: String): Method = m match {
    case GET => Get(m)
    case PUT => Put(m)
    case POST => Post(m)
    case HEAD => Head(m)
    case PATCH => Patch(m)
    case DELETE => Delete(m)
    case OPTIONS => Options(m)
    case _ => throw new RuntimeException(s"$m is not a supported request method.")
  }
}

case class Get(m: String) extends Method
case class Put(m: String) extends Method
case class Post(m: String) extends Method
case class Head(m: String) extends Method
case class Patch(m: String) extends Method
case class Delete(m: String) extends Method
case class Options(m: String) extends Method
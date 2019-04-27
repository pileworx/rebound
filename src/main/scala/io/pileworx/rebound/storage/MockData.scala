package io.pileworx.rebound.storage

import akka.http.scaladsl.model.StatusCode
import akka.http.scaladsl.model.StatusCodes.{Accepted, BadRequest}
import scala.collection.mutable
import scala.collection.immutable.Map

class MockData(engine: TemplateEngine) {

  private var verb = initStorage()

  def getData(v: String, k: String): Option[DefineMockCmd] = {
    if (!verb(v).contains(k)) return None
    Some(verb(v)(k))
  }

  def add(data: DefineMockCmd): StatusCode = {
    if (!verb.contains(data.method)) return BadRequest

    val processed = data.response match {
      case Some(response) => data.values match {
        case Some(values) => data.copy(response = Some(engine.process(response, values)))
        case None => data.copy(response = Some(engine.process(response, Map())))
      }
      case None => data
    }

    verb(processed.method) += makeKey(processed) -> processed
    Accepted
  }

  def reset(): Unit = {
    verb.clear()
    verb = initStorage()
  }

  private def makeKey(data: DefineMockCmd): String = {
    val path = if('/' == data.path.charAt(0)) data.path.substring(1) else data.path
    data.qs match {
      case Some(qs) => s"$path?$qs"
      case _ => path
    }
  }

  private def initStorage() = {
    mutable.Map[String, mutable.Map[String, DefineMockCmd]](
      MockData.GET -> mutable.Map[String, DefineMockCmd](),
      MockData.PUT -> mutable.Map[String, DefineMockCmd](),
      MockData.POST -> mutable.Map[String, DefineMockCmd](),
      MockData.HEAD -> mutable.Map[String, DefineMockCmd](),
      MockData.PATCH -> mutable.Map[String, DefineMockCmd](),
      MockData.DELETE -> mutable.Map[String, DefineMockCmd](),
      MockData.OPTIONS -> mutable.Map[String, DefineMockCmd]()
    )
  }
}

object MockData {
  val GET = "GET"
  val PUT = "PUT"
  val POST = "POST"
  val HEAD = "HEAD"
  val PATCH = "PATCH"
  val DELETE = "DELETE"
  val OPTIONS = "OPTIONS"
}

case class DefineMockCmd(method: String,
                         path: String,
                         qs: Option[String],
                         status: Int,
                         response: Option[String],
                         contentType: String,
                         values: Option[Map[String, String]])
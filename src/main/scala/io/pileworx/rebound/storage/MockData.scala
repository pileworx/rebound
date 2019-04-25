package io.pileworx.rebound.storage

import akka.http.scaladsl.model.StatusCode
import akka.http.scaladsl.model.StatusCodes.{Accepted, BadRequest}
import scala.collection.mutable

object MockData {
  val GET = "GET"
  val PUT = "PUT"
  val POST = "POST"
  val HEAD = "HEAD"
  val PATCH = "PATCH"
  val DELETE = "DELETE"
  val OPTION = "OPTION"

  var verb: mutable.Map[String, mutable.Map[String, DefineMockCmd]] = initStorage()

  def add(data: DefineMockCmd): StatusCode = {
    if (verb.contains(data.method)) {
      verb(data.method) += makeKey(data) -> data
      Accepted
    } else {
      BadRequest
    }
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
      GET -> mutable.Map[String, DefineMockCmd](),
      PUT -> mutable.Map[String, DefineMockCmd](),
      POST -> mutable.Map[String, DefineMockCmd](),
      HEAD -> mutable.Map[String, DefineMockCmd](),
      PATCH -> mutable.Map[String, DefineMockCmd](),
      DELETE -> mutable.Map[String, DefineMockCmd](),
      OPTION -> mutable.Map[String, DefineMockCmd]()
    )
  }
}

case class DefineMockCmd(method: String,
                         path: String,
                         qs: Option[String],
                         status: Int,
                         response: String,
                         contentType: String)

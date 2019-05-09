package io.pileworx.rebound.application

import akka.http.scaladsl.model.StatusCode
import akka.http.scaladsl.model.StatusCodes.{Accepted, BadRequest}
import scala.collection.mutable

class ReboundDao {

  private var verb = initStorage()

  def get(query: MockQuery): Option[DefineMockCmd] = {
    if (!verb(query.verb).contains(query.path))
      None
    else
      Some(verb(query.verb)(query.path))
  }

  def add(data: DefineMockCmd): StatusCode = {
    if (!verb.contains(data.method)) {
      BadRequest
    } else {
      verb(data.method) += makeKey(data) -> data
      Accepted
    }
  }

  def reset(): Unit = {
    verb.clear()
    verb = initStorage()
  }

  private def makeKey(data: DefineMockCmd) = {
    val path = if('/' == data.path.charAt(0)) data.path.substring(1) else data.path
    data.qs match {
      case Some(qs) => s"$path?$qs"
      case _ => path
    }
  }

  private def initStorage() = {
    mutable.Map[String, mutable.Map[String, DefineMockCmd]](
      ReboundDao.GET -> mutable.Map[String, DefineMockCmd](),
      ReboundDao.PUT -> mutable.Map[String, DefineMockCmd](),
      ReboundDao.POST -> mutable.Map[String, DefineMockCmd](),
      ReboundDao.HEAD -> mutable.Map[String, DefineMockCmd](),
      ReboundDao.PATCH -> mutable.Map[String, DefineMockCmd](),
      ReboundDao.DELETE -> mutable.Map[String, DefineMockCmd](),
      ReboundDao.OPTIONS -> mutable.Map[String, DefineMockCmd]()
    )
  }
}

object ReboundDao {
  val GET = "GET"
  val PUT = "PUT"
  val POST = "POST"
  val HEAD = "HEAD"
  val PATCH = "PATCH"
  val DELETE = "DELETE"
  val OPTIONS = "OPTIONS"
}
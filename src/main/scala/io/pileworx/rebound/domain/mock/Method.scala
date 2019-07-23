package io.pileworx.rebound.domain.mock

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

case class Get(m: String = Method.GET) extends Method
case class Put(m: String = Method.PUT) extends Method
case class Post(m: String = Method.POST) extends Method
case class Head(m: String = Method.HEAD) extends Method
case class Patch(m: String = Method.PATCH) extends Method
case class Delete(m: String = Method.DELETE) extends Method
case class Options(m: String = Method.OPTIONS) extends Method

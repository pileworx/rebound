package io.pileworx.rebound.domain.mock

case class Header(name: String, value: String)
object Header {
  val request: Seq[String] = Seq("accept")
  val response: Seq[String] = Seq("content-type")
}
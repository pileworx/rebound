package io.pileworx.rebound.domain.mock

import io.pileworx.rebound.domain.command.DefineResponseCmd

case class Response(status: Int,
                    headers: Option[Seq[Header]],
                    body: Option[String],
                    values: Option[Map[String, String]],
                    used: Boolean = false) {
  def getHeader(name: String): Option[Header] = headers match {
    case Some(hs) => hs.find(h => h.name.equalsIgnoreCase(name))
    case None => None
  }
}
object Response {
  def apply(cmd: DefineResponseCmd): Response = {
    new Response(cmd.status, cmd.headers, cmd.body, cmd.values)
  }
}
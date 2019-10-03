package io.pileworx.rebound.common.logging.formatter

import io.pileworx.rebound.domain.mock.Response

class HttpResponseFormatter {
  def format(resp: Response): String = {
    val body = resp.body match {
      case Some(b) => b
      case None => ""
    }
    val headers = resp.headers match {
      case Some(h) => h
      case None => List()
    }
    s"""
      |  Status: ${resp.status}
      |  Headers:
      |${headers.map(h => s"    ${h.name}: ${h.value}").mkString("\n")}
      |  Body: $body
      |""".stripMargin
  }
}

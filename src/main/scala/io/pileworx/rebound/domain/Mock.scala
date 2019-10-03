package io.pileworx.rebound.domain

import io.pileworx.rebound.common.velocity.TemplateEngine
import io.pileworx.rebound.domain.command.DefineMockCmd
import io.pileworx.rebound.domain.mock.{MockId, Response}

import scala.collection.mutable

case class Mock(id: MockId,
                scenario: String,
                responses: mutable.Seq[Response],
                executed: Boolean = false) {
  def nextResponse(): Option[Response] = {
    if (responses.length == 1) responses.headOption
    else {
      val resp = responses.find(r => !r.used)
      resp match {
        case Some(r) =>
          responses(responses.indexOf(r)) = r.copy(used = true)
          Some(r)
        case _ => None
      }
    }
  }
}

object Mock {
  def apply(cmd: DefineMockCmd, engine: TemplateEngine): Mock = {
    val responses: List[Response] = cmd.`then`.map { resp =>
      resp.body match {
        case Some(b) => resp.values match {
          case Some(values) => Response(resp.copy(body = Some(engine.process(b, values))))
          case _ => Response(resp.copy(body = Some(engine.process(b, Map()))))
        }
        case _ => Response(resp)
      }
    }
    Mock(MockId(cmd), cmd.scenario, mutable.MutableList(responses.toArray:_*))
  }
}
package io.pileworx.rebound.application

import io.pileworx.rebound.common.velocity.TemplateEngine
import io.pileworx.rebound.domain.{Mock, MockRepository}
import io.pileworx.rebound.domain.command.DefineMockCmd
import io.pileworx.rebound.domain.mock.{MockId, Response}

class ReboundService(repository: MockRepository, engine: TemplateEngine) {

  def nextResponseById(id: MockId): Option[Response] = repository.findById(id) match {
    case Some(m) => m.nextResponse()
    case None => None
  }

  def add(cmd: DefineMockCmd): Unit = repository.save(Mock(cmd, engine))

  def clear(): Unit = repository.reset()
}
package io.pileworx.rebound.application

import akka.http.scaladsl.model.StatusCode
import io.pileworx.rebound.common.velocity.TemplateEngine
import io.pileworx.rebound.domain.{Mock, MockId, MockRepository}
import io.pileworx.rebound.domain.command.DefineMockCmd

import scala.collection.immutable.Map

class ReboundService(repository: MockRepository, engine: TemplateEngine) {

  def findById(id: MockId): Option[Mock] = repository.findById(id)


  def add(cmd: DefineMockCmd): StatusCode = {

    val data = cmd.response match {
      case Some(response) => cmd.values match {
        case Some(values) => cmd.copy(response = Some(engine.process(response, values)))
        case None => cmd.copy(response = Some(engine.process(response, Map())))
      }
      case None => cmd
    }

    repository.save(Mock(DefineMockCmd))
  }

  def clear(): Unit = repository.reset()
}

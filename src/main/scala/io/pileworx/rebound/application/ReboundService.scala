package io.pileworx.rebound.application

import akka.http.scaladsl.model.StatusCode
import io.pileworx.rebound.common.velocity.TemplateEngine

import scala.collection.immutable.Map

class ReboundService(reboundDao: ReboundDao, engine: TemplateEngine) {

  def add(cmd: DefineMockCmd): StatusCode = {
    val data = cmd.response match {
      case Some(response) => cmd.values match {
        case Some(values) => cmd.copy(response = Some(engine.process(response, values)))
        case None => cmd.copy(response = Some(engine.process(response, Map())))
      }
      case None => cmd
    }

    reboundDao.add(data)
  }

  def find(query: MockQuery): Option[DefineMockCmd] = reboundDao.get(query)

  def clear(): Unit = reboundDao.reset()
}

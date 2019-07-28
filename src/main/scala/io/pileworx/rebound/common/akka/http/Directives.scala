package io.pileworx.rebound.common.akka.http

import akka.http.scaladsl.server.Directive1
import akka.http.scaladsl.unmarshalling.FromRequestUnmarshaller
import akka.http.scaladsl.server.Directives._

object Directives {

  def optionalEntity[T](unmarshaller: FromRequestUnmarshaller[T]): Directive1[Option[T]] = {
    entity(as[String]).flatMap { stringEntity =>
      if (stringEntity == null || stringEntity.isEmpty) {
        provide(Option.empty[T])
      } else {
        entity(unmarshaller).flatMap(e => provide(Some(e)))
      }
    }
  }
}

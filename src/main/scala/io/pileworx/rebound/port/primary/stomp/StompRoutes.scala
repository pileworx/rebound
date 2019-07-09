package io.pileworx.rebound.port.primary.stomp

import io.pileworx.rebound.common.akka.AkkaImplicits
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import akka.http.websocket.stomp.server.Directives._

class StompRoutes extends AkkaImplicits {

  val routes: Route = path("stomp") {
    stomp(
      topics = Seq(
        "/topic/general"
      )
    )
  }
}
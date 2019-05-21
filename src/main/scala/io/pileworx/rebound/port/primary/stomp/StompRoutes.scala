package io.pileworx.rebound.port.primary.stomp

import akka.http.scaladsl.model.ws.{BinaryMessage, Message, TextMessage}
import akka.stream.scaladsl.{Flow, Sink}
import io.pileworx.rebound.common.akka.AkkaImplicits
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route

class StompRoutes extends AkkaImplicits {
  def stomp: Flow[Message, Message, Any] = Flow[Message].mapConcat {
    case TextMessage.Strict(tm) =>
      val input = tm
      TextMessage(tm) :: Nil
    case bm: BinaryMessage =>
      // ignore binary messages but drain content to avoid the stream being clogged
      bm.dataStream.runWith(Sink.ignore)
      Nil
  }

  val routes: Route = path("stomp") {
    handleWebSocketMessagesForOptionalProtocol(stomp, Some("v12.stomp")) ~
    handleWebSocketMessagesForOptionalProtocol(stomp, Some("v11.stomp")) ~
    handleWebSocketMessagesForOptionalProtocol(stomp, Some("v10.stomp"))
  }
}
//TextMessage.Strict(CONNECT
//accept-version:1.0,1.1,1.2
//heart-beat:4000,4000)
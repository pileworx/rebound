package io.pileworx.rebound.common.akka.http.stomp

import org.scalatest.{Matchers, WordSpec}

class TextFrameParserSpec extends WordSpec with Matchers {

  private val connectFrame = "CONNECT\naccept-version:1.0,1.1,1.2\nheart-beat:4000,4000\n\n^@"
  private val connectFrameWithBody = "CONNECT\naccept-version:1.0,1.1,1.2\nheart-beat:4000,4000\n\n{\"foo\":\"bar\"}^@"

  "TextFrameParser" should {

    "parse a valid connect frame" in {

      val result = new TextFrameParser(connectFrame).parse()

      result shouldBe StompFrame(
        StompCommand.CONNECT,
        Some(Seq(
          StompHeader("accept-version", "1.0,1.1,1.2"),
          StompHeader("heart-beat", "4000,4000")
        )),
        None)
    }

    "fail a connect frame containing a body" in {
      a [FrameException] should be thrownBy {
        new TextFrameParser(connectFrameWithBody).parse()
      }
    }
  }
}

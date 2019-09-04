package io.pileworx.rebound.domain

import io.pileworx.rebound.common.velocity.TemplateEngine
import io.pileworx.rebound.domain.command.{DefineMockCmd, DefineRequestCmd, DefineResponseCmd, When}
import io.pileworx.rebound.domain.mock.{Get, MockId, Response}
import org.scalamock.scalatest.MockFactory
import org.scalatest.{Matchers, WordSpec}

import scala.collection.mutable

class MockSpec extends WordSpec with Matchers with MockFactory {

  val id = MockId(Get(),"",None, None, None)

  "Mock nextResponse" should {

    "return expected response" in {
      val response1 = Response(200, None, Some("body 1"), None)
      val response2 = Response(300, None, Some("body 2"), None)
      val response3 = Response(400, None, Some("body 3"), None)

      val responses: mutable.Seq[Response] = mutable.Seq(response1, response2, response3)
      val mock = Mock(id, "Test", responses)

      val r1 = mock.nextResponse().get
      val r2 = mock.nextResponse().get
      val r3 = mock.nextResponse().get
      val r4 = mock.nextResponse()

      r1 should be theSameInstanceAs response1
      r2 should be theSameInstanceAs response2
      r3 should be theSameInstanceAs response3
      r4 should be(None)
    }

    "return same response when only one response is defined" in {
      val response1 = Response(200, None, Some("body 1"), None)
      val responses: mutable.Seq[Response] = mutable.Seq(response1)
      val mock = Mock(id, "Test", responses)

      val r1 = mock.nextResponse().get
      val r2 = mock.nextResponse().get
      val r3 = mock.nextResponse().get

      r1 should be theSameInstanceAs response1
      r2 should be theSameInstanceAs response1
      r3 should be theSameInstanceAs response1
    }

    "return None when no response is defined" in {
      val responses: mutable.Seq[Response] = mutable.Seq()
      val mock = Mock(id, "Test", responses)

      val r1 = mock.nextResponse()

      r1 should be(None)
    }
  }

  "Mock apply with DefineMockCmd and TemplateEngine" should {

    "process template with supplied values" in {
      val values = Map("key" -> "value")
      val body = "body 1"
      val response = DefineResponseCmd(200, None , Some(body), Some(values))
      val responses = List(response)
      val reqCmd = DefineRequestCmd(Get(), "", None, None, Some("body"))
      val mockCmd = DefineMockCmd("Test", When(reqCmd), responses)
      val engine = mock[TemplateEngine]

      (engine.process _).expects(body, values).returning(body)

      val ar = Mock(mockCmd, engine)

      ar.responses.length shouldEqual 1
    }

    "process template with no supplied values" in {
      val values: Map[String, String] = Map()
      val body = "body 1"
      val response = DefineResponseCmd(200, None , Some(body), None)
      val responses = List(response)
      val reqCmd = DefineRequestCmd(Get(), "", None, None, Some("body"))
      val mockCmd = DefineMockCmd("Test", When(reqCmd), responses)
      val engine = mock[TemplateEngine]

      (engine.process _).expects(body, values).returning(body)

      val ar = Mock(mockCmd, engine)

      ar.responses.length shouldEqual 1
    }

    "not attempt to process template with no body" in {
      val values: Map[String, String] = Map()
      val response = DefineResponseCmd(200, None , None, Some(values))
      val responses = List(response)
      val reqCmd = DefineRequestCmd(Get(), "", None, None, Some("body"))
      val mockCmd = DefineMockCmd("Test", When(reqCmd), responses)
      val engine = mock[TemplateEngine]

      val ar = Mock(mockCmd, engine)

      ar.responses.length shouldEqual 1
    }
  }
}

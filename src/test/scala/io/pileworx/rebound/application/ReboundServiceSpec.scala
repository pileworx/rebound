package io.pileworx.rebound.application

import io.pileworx.rebound.common.velocity.TemplateEngine
import io.pileworx.rebound.domain.{Mock, MockRepository}
import io.pileworx.rebound.domain.command.{DefineMockCmd, DefineRequestCmd, DefineResponseCmd, When}
import io.pileworx.rebound.domain.mock.{Get, MockId, Response}
import org.scalamock.scalatest.MockFactory
import org.scalatest.{Matchers, WordSpec}

class ReboundServiceSpec extends WordSpec with Matchers with MockFactory {

  val values: Map[String, String] = Map()
  val response = DefineResponseCmd(200, None , None, Some(values))
  val responses = List(response)
  val reqCmd = DefineRequestCmd(Get(), "", None, None, Some("body"))
  val cmd = DefineMockCmd("Test", When(reqCmd), responses)

  "Rebound Service" should {

    "send response and values to template engine and store data" in {
      val engineMock = mock[TemplateEngine]
      val repository = mock[MockRepository]
      val service = new ReboundService(repository, engineMock, null)

      (repository.save _).expects(*).returns(Unit)

      service.add(cmd)
    }


    "return next response if a response is found" in {
      val id = mock[MockId]
      val ar = mock[Mock]
      val response = Some(stub[Response])
      val repository = mock[MockRepository]
      val service = new ReboundService(repository, null, null)

      (repository.findById _).expects(id).returning(Some(ar))
      (ar.nextResponse _).expects().returns(response)

      val result = service.nextResponseById(id)
      result should be(response)
    }

    "return None if a response is not found" in {
      val id = mock[MockId]
      val repository = mock[MockRepository]
      val service = new ReboundService(repository, null, null)

      (repository.findById _).expects(id).returning(None)

      val result = service.nextResponseById(id)
      result should be(None)
    }

    "reset repository when clear is called" in {
      val repository = mock[MockRepository]
      val service = new ReboundService(repository, null, null)

      (repository.reset _).expects().returning(Unit)

      service.clear()
    }
  }
}

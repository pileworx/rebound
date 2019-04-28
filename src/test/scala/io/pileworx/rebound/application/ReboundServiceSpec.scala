package io.pileworx.rebound.application

import akka.http.scaladsl.model.StatusCodes.Accepted
import io.pileworx.rebound.common.velocity.TemplateEngine
import org.scalamock.scalatest.MockFactory
import org.scalatest.{Matchers, WordSpec}

class ReboundServiceSpec extends WordSpec with Matchers with MockFactory {

  private val path = "/foo"
  private val qsKey = "foo?foo=bar&bar=baz"
  private val noneCmd = DefineMockCmd(ReboundDao.GET, path, None, 200, None, "application/json", None)
  private val allCmd = DefineMockCmd(ReboundDao.GET, path, Some("foo=bar&bar=baz"), 200, Some("""{ "field" : "value" }"""), "application/json", Some(Map()))

  "Rebound Service" should {

    "send response and values to template engine and store data" in {
      val engineMock = mock[TemplateEngine]
      val daoMock = mock[ReboundDao]
      val service = new ReboundService(daoMock, engineMock)

      (engineMock.process _).expects(allCmd.response.get, allCmd.values.get).returning(allCmd.response.get)
      (daoMock.add _).expects(*).returning(Accepted)

      val result = service.add(allCmd)
      result should be(Accepted)
    }

    "send response and empty map to template engine and store data" in {
      val responseCmd = allCmd.copy(values = None)
      val engineMock = mock[TemplateEngine]
      val daoMock = mock[ReboundDao]
      val service = new ReboundService(daoMock, engineMock)

      (engineMock.process _).expects(responseCmd.response.get, *).returning(responseCmd.response.get)
      (daoMock.add _).expects(*).returning(Accepted)

      val result = service.add(responseCmd)
      result should be(Accepted)
    }

    "store data without processing if response is not present" in {
      val daoMock = mock[ReboundDao]
      val service = new ReboundService(daoMock, null)

      (daoMock.add _).expects(*).returning(Accepted)

      val result = service.add(noneCmd)
      result should be(Accepted)
    }

    "query dao when find is called" in {
      val query = MockQuery(ReboundDao.GET, qsKey)
      val response = Some(noneCmd)
      val daoMock = mock[ReboundDao]
      val service = new ReboundService(daoMock, null)

      (daoMock.get _).expects(query).returning(response)

      val result = service.find(query)
      result should be(response)
    }

    "reset dao when clear is called" in {
      val daoMock = mock[ReboundDao]
      val service = new ReboundService(daoMock, null)

      (daoMock.reset _).expects().returning(Unit)

      service.clear()
    }
  }
}

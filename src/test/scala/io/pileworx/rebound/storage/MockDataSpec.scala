package io.pileworx.rebound.storage

import org.scalatest.{Matchers, WordSpec}
import akka.http.scaladsl.model.StatusCodes.{Accepted, BadRequest}
import org.scalamock.scalatest.MockFactory

class MockDataSpec extends WordSpec with Matchers with MockFactory {

  private val path = "/foo"
  private val pKey = "foo"
  private val qsKey = "foo?foo=bar&bar=baz"
  private val engineMock = stub[TemplateEngine]
  private val mockData = new MockData(engineMock)
  private val noneCmd = DefineMockCmd(MockData.GET, path, None, 200, None, "application/json", None)
  private val allCmd = DefineMockCmd(MockData.GET, path, Some("foo=bar&bar=baz"), 200, Some("""{ "feild" : "value" }"""), "application/json", Some(Map()))

  "Mock Data Store" should {

    "add data with a key successfully with just a path" in {
      val result = mockData.add(noneCmd)
      result should be(Accepted)
    }

    "get data should return a mock that was loaded with just a path" in {
      val result = mockData.getData(MockData.GET, pKey)
      result.get should be(noneCmd)
    }

    "add data with a key successfully with path and query string" in {
      (engineMock.process _).when(allCmd.response.get, allCmd.values.get).returning(allCmd.response.get)
      val result = mockData.add(allCmd)
      result should be(Accepted)
    }

    "get data should return a mock that was loaded with path and query string" in {
      val result = mockData.getData(MockData.GET, qsKey)
      result.get should be(allCmd)
    }

    "add data successfully with a response and no values " in {
      val novaluesCmd = allCmd.copy(values = None)
      (engineMock.process _).when(novaluesCmd.response.get, *).returning(novaluesCmd.response.get)
      val result = mockData.add(novaluesCmd)
      result should be(Accepted)
    }

    "successfully add and retrieve mock for PUT" in {
      val putCmd = noneCmd.copy(method = MockData.PUT)

      val resultAdd = mockData.add(putCmd)
      val resultGet = mockData.getData(MockData.PUT, pKey)

      resultGet.get should be(putCmd)
      resultAdd should be(Accepted)
    }

    "successfully add and retrieve mock for POST" in {
      val postCmd = noneCmd.copy(method = MockData.POST)

      val resultAdd = mockData.add(postCmd)
      val resultGet = mockData.getData(MockData.POST, pKey)

      resultGet.get should be(postCmd)
      resultAdd should be(Accepted)
    }

    "successfully add and retrieve mock for HEAD" in {
      val headCmd = noneCmd.copy(method = MockData.HEAD)

      val resultAdd = mockData.add(headCmd)
      val resultGet = mockData.getData(MockData.HEAD, pKey)

      resultGet.get should be(headCmd)
      resultAdd should be(Accepted)
    }

    "successfully add and retrieve mock for PATCH" in {
      val patchCmd = noneCmd.copy(method = MockData.PATCH)

      val resultAdd = mockData.add(patchCmd)
      val resultGet = mockData.getData(MockData.PATCH, pKey)

      resultGet.get should be(patchCmd)
      resultAdd should be(Accepted)
    }

    "successfully add and retrieve mock for DELETE" in {
      val deleteCmd = noneCmd.copy(method = MockData.DELETE)

      val resultAdd = mockData.add(deleteCmd)
      val resultGet = mockData.getData(MockData.DELETE, pKey)

      resultGet.get should be(deleteCmd)
      resultAdd should be(Accepted)
    }

    "successfully add and retrieve mock for OPTIONS" in {
      val optionsCmd = noneCmd.copy(method = MockData.OPTIONS)

      val resultAdd = mockData.add(optionsCmd)
      val resultGet = mockData.getData(MockData.OPTIONS, pKey)

      resultGet.get should be(optionsCmd)
      resultAdd should be(Accepted)
    }

    "not add mock for invalid method" in {
      val notaverbCmd = noneCmd.copy(method = "NOTAVERB")

      val resultAdd = mockData.add(notaverbCmd)

      resultAdd should be(BadRequest)
    }

    "not require / at path start" in {
      val noslashCmd = noneCmd.copy(path = "foo")

      val resultAdd = mockData.add(noslashCmd)
      val resultGet = mockData.getData(MockData.GET, pKey)

      resultGet.get should be(noslashCmd)
      resultAdd should be(Accepted)
    }

    "clear all data in storage when reset is called" in {
      mockData.reset()
      val resultP = mockData.getData(MockData.GET, pKey)
      val resultQs = mockData.getData(MockData.GET, qsKey)

      resultP should be(None)
      resultQs should be(None)
    }
  }
}
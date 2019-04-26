package io.pileworx.rebound.storage

import org.scalatest.{Matchers, WordSpec}
import akka.http.scaladsl.model.StatusCodes.{Accepted, BadRequest}

class MockDataSpec extends WordSpec with Matchers {

  val path = "/foo"
  val pKey = "foo"
  val qsKey = "foo?foo=bar&bar=baz"
  val noneCmd = DefineMockCmd(MockData.GET, path, None, 200, None, "application/json")
  val allCmd = DefineMockCmd(MockData.GET, path, Some("foo=bar&bar=baz"), 200, Some("""{ "feild" : "value" }"""), "application/json")

  "Mock Data Store " should {

    "add data with a key successfully with just a path" in {
      val result = MockData.add(noneCmd)
      result should be(Accepted)
    }

    "get data should return a mock that was loaded with just a path" in {
      val result = MockData.getData(MockData.GET, pKey)
      result.get should be(noneCmd)
    }

    "add data with a key successfully with path and query string" in {
      val result = MockData.add(allCmd)
      result should be(Accepted)
    }

    "get data should return a mock that was loaded with path and query string" in {
      val result = MockData.getData(MockData.GET, qsKey)
      result.get should be(allCmd)
    }

    "successfully add and retrieve mock for PUT" in {
      val putCmd = noneCmd.copy(method = MockData.PUT)

      val resultAdd = MockData.add(putCmd)
      val resultGet = MockData.getData(MockData.PUT, pKey)

      resultGet.get should be(putCmd)
      resultAdd should be(Accepted)
    }

    "successfully add and retrieve mock for POST" in {
      val postCmd = noneCmd.copy(method = MockData.POST)

      val resultAdd = MockData.add(postCmd)
      val resultGet = MockData.getData(MockData.POST, pKey)

      resultGet.get should be(postCmd)
      resultAdd should be(Accepted)
    }

    "successfully add and retrieve mock for HEAD" in {
      val headCmd = noneCmd.copy(method = MockData.HEAD)

      val resultAdd = MockData.add(headCmd)
      val resultGet = MockData.getData(MockData.HEAD, pKey)

      resultGet.get should be(headCmd)
      resultAdd should be(Accepted)
    }

    "successfully add and retrieve mock for PATCH" in {
      val patchCmd = noneCmd.copy(method = MockData.PATCH)

      val resultAdd = MockData.add(patchCmd)
      val resultGet = MockData.getData(MockData.PATCH, pKey)

      resultGet.get should be(patchCmd)
      resultAdd should be(Accepted)
    }

    "successfully add and retrieve mock for DELETE" in {
      val deleteCmd = noneCmd.copy(method = MockData.DELETE)

      val resultAdd = MockData.add(deleteCmd)
      val resultGet = MockData.getData(MockData.DELETE, pKey)

      resultGet.get should be(deleteCmd)
      resultAdd should be(Accepted)
    }

    "successfully add and retrieve mock for OPTIONS" in {
      val optionsCmd = noneCmd.copy(method = MockData.OPTIONS)

      val resultAdd = MockData.add(optionsCmd)
      val resultGet = MockData.getData(MockData.OPTIONS, pKey)

      resultGet.get should be(optionsCmd)
      resultAdd should be(Accepted)
    }

    "not add mock for invalid method" in {
      val notaverbCmd = noneCmd.copy(method = "NOTAVERB")

      val resultAdd = MockData.add(notaverbCmd)

      resultAdd should be(BadRequest)
    }

    "clear all data in storage when reset is called" in {
      MockData.reset()
      val resultP = MockData.getData(MockData.GET, pKey)
      val resultQs = MockData.getData(MockData.GET, qsKey)

      resultP should be(None)
      resultQs should be(None)
    }
  }
}
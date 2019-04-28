package io.pileworx.rebound.application

import akka.http.scaladsl.model.StatusCodes.{Accepted, BadRequest}
import org.scalamock.scalatest.MockFactory
import org.scalatest.{Matchers, WordSpec}

class ReboundDaoSpec extends WordSpec with Matchers with MockFactory {

  private val path = "/foo"
  private val pKey = "foo"
  private val qsKey = "foo?foo=bar&bar=baz"
  private val mockData = new ReboundDao
  private val noneCmd = DefineMockCmd(ReboundDao.GET, path, None, 200, None, "application/json", None)
  private val allCmd = DefineMockCmd(ReboundDao.GET, path, Some("foo=bar&bar=baz"), 200, Some("""{ "field" : "value" }"""), "application/json", Some(Map()))

  "Mock Data Store" should {

    "add data with a key successfully with just a path" in {
      val result = mockData.add(noneCmd)
      result should be(Accepted)
    }

    "get data should return a mock that was loaded with just a path" in {
      val result = mockData.get(MockQuery(ReboundDao.GET, pKey))
      result.get should be(noneCmd)
    }

    "add data with a key successfully with path and query string" in {
      val result = mockData.add(allCmd)
      result should be(Accepted)
    }

    "get data should return a mock that was loaded with path and query string" in {
      val result = mockData.get(MockQuery(ReboundDao.GET, qsKey))
      result.get should be(allCmd)
    }

    "successfully add and retrieve mock for PUT" in {
      val putCmd = noneCmd.copy(method = ReboundDao.PUT)

      val resultAdd = mockData.add(putCmd)
      val resultGet = mockData.get(MockQuery(ReboundDao.PUT, pKey))

      resultGet.get should be(putCmd)
      resultAdd should be(Accepted)
    }

    "successfully add and retrieve mock for POST" in {
      val postCmd = noneCmd.copy(method = ReboundDao.POST)

      val resultAdd = mockData.add(postCmd)
      val resultGet = mockData.get(MockQuery(ReboundDao.POST, pKey))

      resultGet.get should be(postCmd)
      resultAdd should be(Accepted)
    }

    "successfully add and retrieve mock for HEAD" in {
      val headCmd = noneCmd.copy(method = ReboundDao.HEAD)

      val resultAdd = mockData.add(headCmd)
      val resultGet = mockData.get(MockQuery(ReboundDao.HEAD, pKey))

      resultGet.get should be(headCmd)
      resultAdd should be(Accepted)
    }

    "successfully add and retrieve mock for PATCH" in {
      val patchCmd = noneCmd.copy(method = ReboundDao.PATCH)

      val resultAdd = mockData.add(patchCmd)
      val resultGet = mockData.get(MockQuery(ReboundDao.PATCH, pKey))

      resultGet.get should be(patchCmd)
      resultAdd should be(Accepted)
    }

    "successfully add and retrieve mock for DELETE" in {
      val deleteCmd = noneCmd.copy(method = ReboundDao.DELETE)

      val resultAdd = mockData.add(deleteCmd)
      val resultGet = mockData.get(MockQuery(ReboundDao.DELETE, pKey))

      resultGet.get should be(deleteCmd)
      resultAdd should be(Accepted)
    }

    "successfully add and retrieve mock for OPTIONS" in {
      val optionsCmd = noneCmd.copy(method = ReboundDao.OPTIONS)

      val resultAdd = mockData.add(optionsCmd)
      val resultGet = mockData.get(MockQuery(ReboundDao.OPTIONS, pKey))

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
      val resultGet = mockData.get(MockQuery(ReboundDao.GET, pKey))

      resultGet.get should be(noslashCmd)
      resultAdd should be(Accepted)
    }

    "clear all data in storage when reset is called" in {
      mockData.reset()
      val resultP = mockData.get(MockQuery(ReboundDao.GET, pKey))
      val resultQs = mockData.get(MockQuery(ReboundDao.GET, qsKey))

      resultP should be(None)
      resultQs should be(None)
    }
  }
}
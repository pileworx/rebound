package io.pileworx.rebound.port.primary.rest

import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.testkit.ScalatestRouteTest
import io.pileworx.rebound.application.{DefineMockCmd, MockQuery, ReboundDao, ReboundService}
import org.scalamock.scalatest.MockFactory
import org.scalatest.{Matchers, WordSpec}

class ReboundRoutesSpec extends WordSpec with Matchers with ScalatestRouteTest with MockFactory {

  private val serverResponse = """{"propertyName":"this is my value"}"""

  private val allCmd = DefineMockCmd(
    "PUT",
    "/batman/location",
    Some("foo=bar&bar=baz"),
    201,
    Some("{\"propertyName\":\"this is my value\"}"),
    "application/hal+json",
    Some(Map())
  )

  private val requiredCmd = DefineMockCmd(
    "PUT",
    "/batman/location",
    None,
    201,
    None,
    "application/hal+json",
    None
  )

  private val mKey = "foo"
  private val mQsKey = "foo?foo=bar&bar=baz"

  "Rebound routes" should {

    "GET response with payload" in {
      val serviceMock = stub[ReboundService]
      val route = new ReboundRoutes(serviceMock)

      (serviceMock.find _).when(MockQuery(ReboundDao.GET, mKey)).returning(Some(allCmd))

      Get(s"/$mKey") ~> route.routes ~> check {
        status shouldEqual StatusCodes.Created
        responseAs[String] should include(serverResponse)
      }
    }

    "GET response with payload by query string" in {
      val serviceMock = stub[ReboundService]
      val route = new ReboundRoutes(serviceMock)

      (serviceMock.find _).when(MockQuery(ReboundDao.GET, mQsKey)).returning(Some(allCmd))

      Get(s"/$mQsKey") ~> route.routes ~> check {
        status shouldEqual StatusCodes.Created
        responseAs[String] should include(serverResponse)
      }
    }

    "GET with no response that doesn't match key" in {
      val serviceMock = stub[ReboundService]
      val route = new ReboundRoutes(serviceMock)

      (serviceMock.find _).when(MockQuery(ReboundDao.GET, mKey)).returning(None)

      Get(s"/$mKey") ~> route.routes ~> check {
        status shouldEqual StatusCodes.BadRequest
      }
    }

    "PUT response with payload" in {
      val serviceMock = stub[ReboundService]
      val route = new ReboundRoutes(serviceMock)

      (serviceMock.find _).when(MockQuery(ReboundDao.PUT, mKey)).returning(Some(allCmd))

      Put(s"/$mKey") ~> route.routes ~> check {
        status shouldEqual StatusCodes.Created
        responseAs[String] should include(serverResponse)
      }
    }

    "PUT response with payload by query string" in {
      val serviceMock = stub[ReboundService]
      val route = new ReboundRoutes(serviceMock)

      (serviceMock.find _).when(MockQuery(ReboundDao.PUT, mQsKey)).returning(Some(allCmd))

      Put(s"/$mQsKey") ~> route.routes ~> check {
        status shouldEqual StatusCodes.Created
        responseAs[String] should include(serverResponse)
      }
    }

    "PUT with no response that doesn't match key" in {
      val serviceMock = stub[ReboundService]
      val route = new ReboundRoutes(serviceMock)

      (serviceMock.find _).when(MockQuery(ReboundDao.PUT, mKey)).returning(None)

      Put(s"/$mKey") ~> route.routes ~> check {
        status shouldEqual StatusCodes.BadRequest
      }
    }

    "POST response with payload" in {
      val serviceMock = stub[ReboundService]
      val route = new ReboundRoutes(serviceMock)

      (serviceMock.find _).when(MockQuery(ReboundDao.POST, mKey)).returning(Some(allCmd))

      Post(s"/$mKey") ~> route.routes ~> check {
        status shouldEqual StatusCodes.Created
        responseAs[String] should include(serverResponse)
      }
    }

    "POST response with payload by query string" in {
      val serviceMock = stub[ReboundService]
      val route = new ReboundRoutes(serviceMock)

      (serviceMock.find _).when(MockQuery(ReboundDao.POST, mQsKey)).returning(Some(allCmd))

      Post(s"/$mQsKey") ~> route.routes ~> check {
        status shouldEqual StatusCodes.Created
        responseAs[String] should include(serverResponse)
      }
    }

    "POST with no response that doesn't match key" in {
      val serviceMock = stub[ReboundService]
      val route = new ReboundRoutes(serviceMock)

      (serviceMock.find _).when(MockQuery(ReboundDao.POST, mKey)).returning(None)

      Post(s"/$mKey") ~> route.routes ~> check {
        status shouldEqual StatusCodes.BadRequest
      }
    }

    "HEAD is not implemented" in {
      val serviceMock = stub[ReboundService]
      val route = new ReboundRoutes(serviceMock)

      Head(s"/$mKey") ~> route.routes ~> check {
        status shouldEqual StatusCodes.MethodNotAllowed
      }
    }

    "PATCH response with payload" in {
      val serviceMock = stub[ReboundService]
      val route = new ReboundRoutes(serviceMock)

      (serviceMock.find _).when(MockQuery(ReboundDao.PATCH, mKey)).returning(Some(allCmd))

      Patch(s"/$mKey") ~> route.routes ~> check {
        status shouldEqual StatusCodes.Created
        responseAs[String] should include(serverResponse)
      }
    }

    "PATCH response with payload by query string" in {
      val serviceMock = stub[ReboundService]
      val route = new ReboundRoutes(serviceMock)

      (serviceMock.find _).when(MockQuery(ReboundDao.PATCH, mQsKey)).returning(Some(allCmd))

      Patch(s"/$mQsKey") ~> route.routes ~> check {
        status shouldEqual StatusCodes.Created
        responseAs[String] should include(serverResponse)
      }
    }

    "PATCH with no response that doesn't match key" in {
      val serviceMock = stub[ReboundService]
      val route = new ReboundRoutes(serviceMock)

      (serviceMock.find _).when(MockQuery(ReboundDao.PATCH, mKey)).returning(None)

      Patch(s"/$mKey") ~> route.routes ~> check {
        status shouldEqual StatusCodes.BadRequest
      }
    }

    "DELETE response with payload" in {
      val serviceMock = stub[ReboundService]
      val route = new ReboundRoutes(serviceMock)

      (serviceMock.find _).when(MockQuery(ReboundDao.DELETE, mKey)).returning(Some(allCmd))

      Delete(s"/$mKey") ~> route.routes ~> check {
        status shouldEqual StatusCodes.Created
        responseAs[String] should include(serverResponse)
      }
    }

    "DELETE response with payload by query string" in {
      val serviceMock = stub[ReboundService]
      val route = new ReboundRoutes(serviceMock)

      (serviceMock.find _).when(MockQuery(ReboundDao.DELETE, mQsKey)).returning(Some(allCmd))

      Delete(s"/$mQsKey") ~> route.routes ~> check {
        status shouldEqual StatusCodes.Created
        responseAs[String] should include(serverResponse)
      }
    }

    "DELETE with no response that doesn't match key" in {
      val serviceMock = stub[ReboundService]
      val route = new ReboundRoutes(serviceMock)

      (serviceMock.find _).when(MockQuery(ReboundDao.DELETE, mKey)).returning(None)

      Delete(s"/$mKey") ~> route.routes ~> check {
        status shouldEqual StatusCodes.BadRequest
      }
    }

    "OPTIONS is not implemented" in {
      val serviceMock = stub[ReboundService]
      val route = new ReboundRoutes(serviceMock)

      Options(s"/$mKey") ~> route.routes ~> check {
        status shouldEqual StatusCodes.MethodNotAllowed
      }
    }
  }
}
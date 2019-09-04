package io.pileworx.rebound.port.primary.rest

import akka.http.scaladsl.model.{HttpCharsets, MediaTypes, StatusCodes}
import akka.http.scaladsl.testkit.ScalatestRouteTest
import io.pileworx.rebound.application.ReboundService
import io.pileworx.rebound.domain.mock.{Header, Response}
import org.scalamock.scalatest.MockFactory
import org.scalatest.{Matchers, WordSpec}

class ReboundRoutesSpec extends WordSpec with Matchers with ScalatestRouteTest with MockFactory {

  private val serverResponse = """{"propertyName":"this is my value"}"""

  private val resp: Response = Response(
    201,
    Some(Seq(Header("Content-Type", "application/hal+json"))),
    Some("{\"propertyName\":\"this is my value\"}"),
    None)

  private val respNoContentType: Response = Response(
    201,
    None,
    Some("{\"propertyName\":\"this is my value\"}"),
    None)

  private val mKey = "foo"
  private val mQsKey = "foo?foo=bar&bar=baz"

  "Rebound routes" should {

    "GET response with payload" in {
      val serviceMock = stub[ReboundService]
      val route = new ReboundRoutes(serviceMock)

      (serviceMock.nextResponseById _).when(*).returning(Some(resp))

      Get(s"/$mKey") ~> route.routes ~> check {
        status shouldEqual StatusCodes.Created
        responseAs[String] should include(serverResponse)
      }
    }

    "GET response with payload by query string" in {
      val serviceMock = stub[ReboundService]
      val route = new ReboundRoutes(serviceMock)

      (serviceMock.nextResponseById _).when(*).returning(Some(resp))

      Get(s"/$mQsKey") ~> route.routes ~> check {
        status shouldEqual StatusCodes.Created
        responseAs[String] should include(serverResponse)
      }
    }

    "GET with no response that doesn't match key" in {
      val serviceMock = stub[ReboundService]
      val route = new ReboundRoutes(serviceMock)

      (serviceMock.nextResponseById _).when(*).returning(None)

      Get(s"/$mKey") ~> route.routes ~> check {
        status shouldEqual StatusCodes.BadRequest
      }
    }

    "PUT response with payload" in {
      val serviceMock = stub[ReboundService]
      val route = new ReboundRoutes(serviceMock)

      (serviceMock.nextResponseById _).when(*).returning(Some(resp))

      Put(s"/$mKey") ~> route.routes ~> check {
        status shouldEqual StatusCodes.Created
        responseAs[String] should include(serverResponse)
      }
    }

    "PUT response with payload by query string" in {
      val serviceMock = stub[ReboundService]
      val route = new ReboundRoutes(serviceMock)

      (serviceMock.nextResponseById _).when(*).returning(Some(resp))

      Put(s"/$mQsKey") ~> route.routes ~> check {
        status shouldEqual StatusCodes.Created
        responseAs[String] should include(serverResponse)
      }
    }

    "PUT with no response that doesn't match key" in {
      val serviceMock = stub[ReboundService]
      val route = new ReboundRoutes(serviceMock)

      (serviceMock.nextResponseById _).when(*).returning(None)

      Put(s"/$mKey") ~> route.routes ~> check {
        status shouldEqual StatusCodes.BadRequest
      }
    }

    "POST response with payload" in {
      val serviceMock = stub[ReboundService]
      val route = new ReboundRoutes(serviceMock)

      (serviceMock.nextResponseById _).when(*).returning(Some(resp))

      Post(s"/$mKey") ~> route.routes ~> check {
        status shouldEqual StatusCodes.Created
        responseAs[String] should include(serverResponse)
      }
    }

    "POST response with payload by query string" in {
      val serviceMock = stub[ReboundService]
      val route = new ReboundRoutes(serviceMock)

      (serviceMock.nextResponseById _).when(*).returning(Some(resp))

      Post(s"/$mQsKey") ~> route.routes ~> check {
        status shouldEqual StatusCodes.Created
        responseAs[String] should include(serverResponse)
      }
    }

    "POST with no response that doesn't match key" in {
      val serviceMock = stub[ReboundService]
      val route = new ReboundRoutes(serviceMock)

      (serviceMock.nextResponseById _).when(*).returning(None)

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

      (serviceMock.nextResponseById _).when(*).returning(Some(resp))

      Patch(s"/$mKey") ~> route.routes ~> check {
        status shouldEqual StatusCodes.Created
        responseAs[String] should include(serverResponse)
      }
    }

    "PATCH response with payload by query string" in {
      val serviceMock = stub[ReboundService]
      val route = new ReboundRoutes(serviceMock)

      (serviceMock.nextResponseById _).when(*).returning(Some(resp))

      Patch(s"/$mQsKey") ~> route.routes ~> check {
        status shouldEqual StatusCodes.Created
        responseAs[String] should include(serverResponse)
      }
    }

    "PATCH with no response that doesn't match key" in {
      val serviceMock = stub[ReboundService]
      val route = new ReboundRoutes(serviceMock)

      (serviceMock.nextResponseById _).when(*).returning(None)

      Patch(s"/$mKey") ~> route.routes ~> check {
        status shouldEqual StatusCodes.BadRequest
      }
    }

    "DELETE response with payload" in {
      val serviceMock = stub[ReboundService]
      val route = new ReboundRoutes(serviceMock)

      (serviceMock.nextResponseById _).when(*).returning(Some(resp))

      Delete(s"/$mKey") ~> route.routes ~> check {
        status shouldEqual StatusCodes.Created
        responseAs[String] should include(serverResponse)
      }
    }

    "DELETE response with payload by query string" in {
      val serviceMock = stub[ReboundService]
      val route = new ReboundRoutes(serviceMock)

      (serviceMock.nextResponseById _).when(*).returning(Some(resp))

      Delete(s"/$mQsKey") ~> route.routes ~> check {
        status shouldEqual StatusCodes.Created
        responseAs[String] should include(serverResponse)
      }
    }

    "DELETE with no response that doesn't match key" in {
      val serviceMock = stub[ReboundService]
      val route = new ReboundRoutes(serviceMock)

      (serviceMock.nextResponseById _).when(*).returning(None)

      Delete(s"/$mKey") ~> route.routes ~> check {
        status shouldEqual StatusCodes.BadRequest
      }
    }

    "return text/plain if not Content-Type is defined" in {
      val serviceMock = stub[ReboundService]
      val route = new ReboundRoutes(serviceMock)

      (serviceMock.nextResponseById _).when(*).returning(Some(respNoContentType))

      Delete(s"/$mKey") ~> route.routes ~> check {
        contentType shouldEqual MediaTypes.`text/plain`.withCharset(HttpCharsets.`UTF-8`)
      }
    }
  }
}
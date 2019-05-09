package io.pileworx.rebound.port.primary.rest

import akka.http.scaladsl.model.StatusCodes.{Accepted, BadRequest, Conflict}
import akka.http.scaladsl.model.{ContentTypes, HttpEntity, StatusCodes}
import akka.http.scaladsl.testkit.ScalatestRouteTest
import io.pileworx.rebound.application.ReboundService
import org.scalamock.scalatest.MockFactory
import org.scalatest.{Matchers, WordSpec}

class MockRoutesSpec extends WordSpec with Matchers with ScalatestRouteTest with MockFactory {

  private val putBody =
    """
      |{
      |  "method": "PUT",
      |  "path": "/batman/location",
      |  "status": 201,
      |  "qs": "foo=bar&bar=baz",
      |  "response": "{\"propertyName\":\"this is my value\"}",
      |  "contentType": "application/hal+json",
      |  "values": {
      |     "myValue": "some value"
      |  }
      |}
    """.stripMargin

  "Mocking routes" should {

    "successfully PUT a mock and receive a valid response " in {
      val serviceMock = stub[ReboundService]
      val route = new MockRoutes(serviceMock)

      (serviceMock.add _).when(*).returning(Accepted)

      Put("/mock", HttpEntity(ContentTypes.`application/json`, putBody)) ~> route.routes ~> check {
        status shouldEqual StatusCodes.Accepted
        responseAs[String] should include(route.acceptMessage)
      }
    }

    "unsuccessfully PUT a mock and receive a valid response" in {
      val serviceMock = stub[ReboundService]
      val route = new MockRoutes(serviceMock)

      (serviceMock.add _).when(*).returning(BadRequest)

      Put("/mock", HttpEntity(ContentTypes.`application/json`, putBody)) ~> route.routes ~> check {
        status shouldEqual StatusCodes.BadRequest
        responseAs[String] should include(route.badRequestMessage)
      }
    }

    "return a internal server error if conditions are not matched" in {
      val serviceMock = stub[ReboundService]
      val route = new MockRoutes(serviceMock)

      (serviceMock.add _).when(*).returning(Conflict)

      Put("/mock", HttpEntity(ContentTypes.`application/json`, putBody)) ~> route.routes ~> check {
        status shouldEqual StatusCodes.InternalServerError
        responseAs[String] should include(route.internalServerErrorMessage)
      }
    }

    "return successfully when a DELETE is sent" in {
      val serviceMock = stub[ReboundService]
      val route = new MockRoutes(serviceMock)

      (serviceMock.clear _).when().returning(Unit)

      Delete("/mock") ~> route.routes ~> check {
        status shouldEqual StatusCodes.OK
        responseAs[String] should include(route.successMessage)
      }
    }
  }
}
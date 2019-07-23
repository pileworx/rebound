package io.pileworx.rebound.port.primary.rest

import akka.http.scaladsl.model.{ContentTypes, HttpEntity, StatusCodes}
import akka.http.scaladsl.testkit.ScalatestRouteTest
import io.pileworx.rebound.application.ReboundService
import org.scalamock.scalatest.MockFactory
import org.scalatest.{Matchers, WordSpec}

class MockRoutesSpec extends WordSpec with Matchers with ScalatestRouteTest with MockFactory {

  private val putBody =
    """
      |{
      |  "scenario": "Scenario Name",
      |  "when": {
      |    "request": {
      |      "method": "PUT",
      |      "path": "/foo/bar",
      |      "qs": "foo=bar&bar=baz",
      |      "headers": [
      |        {
      |          "name": "accept",
      |          "value": "application/json"
      |        }
      |      ],
      |      "body": "{\"foo\":\"bar\"}"
      |    }
      |  },
      |  "then": [
      |    {
      |      "status": 200,
      |      "headers": [
      |        {
      |          "name": "accept",
      |          "value": "application/json"
      |        }
      |      ],
      |      "body": "{\"foo\":\"$bar\"}",
      |      "values": {
      |        "bar": "this is my first value"
      |      }
      |    },
      |    {
      |      "status": 200,
      |      "headers": [
      |        {
      |          "name": "accept",
      |          "value": "application/json"
      |        }
      |      ],
      |      "body": "{\"foo\":\"bar\"}"
      |    }
      |  ]
      |}
    """.stripMargin

  "Mocking routes" should {

    "successfully PUT a mock and receive a valid response " in {
      val serviceMock = stub[ReboundService]
      val route = new MockRoutes(serviceMock)

      (serviceMock.add _).when(*).returning(Unit)

      Put("/mock", HttpEntity(ContentTypes.`application/json`, putBody)) ~> route.routes ~> check {
        status shouldEqual StatusCodes.Accepted
        responseAs[String] should include(route.acceptMessage)
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
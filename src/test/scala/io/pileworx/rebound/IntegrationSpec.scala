package io.pileworx.rebound

import akka.http.scaladsl.model.headers.RawHeader
import akka.http.scaladsl.model.{ContentTypes, HttpEntity, StatusCodes}
import akka.http.scaladsl.testkit.ScalatestRouteTest
import io.pileworx.rebound.application.ReboundService
import io.pileworx.rebound.common.velocity.TemplateEngine
import io.pileworx.rebound.domain.MockRepository
import io.pileworx.rebound.port.primary.rest.{MockRoutes, ReboundRoutes}
import org.scalatest.{Matchers, WordSpec}

class IntegrationSpec extends WordSpec with Matchers with ScalatestRouteTest {
  val engine = new TemplateEngine
  val repository = new MockRepository
  val service = new ReboundService(repository, engine)
  val reboundRoute = new ReboundRoutes(service)
  val mockRoute = new MockRoutes(service)

  private val putBody =
    """
      |{
      |  "scenario": "Scenario Name",
      |  "when": {
      |    "request": {
      |      "method": "PUT",
      |      "path": "/foo/bar",
      |      "query": "foo=bar&bar=baz",
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

  "Integration" should {
    "create a mock" in {
      Put("/mock", HttpEntity(ContentTypes.`application/json`, putBody)) ~> mockRoute.routes ~> check {
        status shouldEqual StatusCodes.Accepted
        responseAs[String] should include(mockRoute.acceptMessage)
      }
    }

    "retrieve first response" in {
      Put("/foo/bar?foo=bar&bar=baz", "{\"foo\":\"bar\"}") ~>
        addHeader(RawHeader("Accept", "application/json")) ~>
        reboundRoute.routes ~> check {

        status shouldEqual StatusCodes.OK
        responseAs[String] should include("this is my first value")
      }
    }

    "retrieve second response" in {
      Put("/foo/bar?foo=bar&bar=baz", "{\"foo\":\"bar\"}") ~>
        addHeader(RawHeader("Accept", "application/json")) ~>
        reboundRoute.routes ~> check {

        status shouldEqual StatusCodes.OK
        responseAs[String] should include("bar")
      }
    }
  }
}
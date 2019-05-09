package io.pileworx.rebound.common.velocity

import akka.http.scaladsl.model.HttpMethods.{DELETE, GET, HEAD, OPTIONS, PATCH, POST, PUT}
import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.model.StatusCodes.Accepted
import akka.http.scaladsl.testkit.ScalatestRouteTest
import io.pileworx.rebound.application.ReboundService
import io.pileworx.rebound.common.akka.http.Cors
import io.pileworx.rebound.port.primary.rest.MockRoutes
import org.scalamock.scalatest.MockFactory
import org.scalatest.{Matchers, WordSpec}
import akka.http.scaladsl.model.headers._

class CorsSpec extends WordSpec with Matchers with ScalatestRouteTest with MockFactory with Cors {

  "Cors route" should {

    "successfully add cors headers to response" in {
      val serviceMock = stub[ReboundService]
      val route = new MockRoutes(serviceMock)

      (serviceMock.add _).when(*).returning(Accepted)

      Options("/mock") ~> cors(route.routes) ~> check {
        status shouldEqual StatusCodes.OK
        header("Access-Control-Allow-Origin") shouldEqual Some(`Access-Control-Allow-Origin`.*)
        header("Access-Control-Allow-Credentials") shouldEqual Some(`Access-Control-Allow-Credentials`(true))
        header("Access-Control-Max-Age") shouldEqual Some(`Access-Control-Max-Age`(3600))
        header("Access-Control-Allow-Headers") shouldEqual Some(`Access-Control-Allow-Headers`("Origin", "x-requested-with", "Content-Type", "Accept", "Authorization", "Allow"))
        header("Access-Control-Allow-Methods") shouldEqual Some(`Access-Control-Allow-Methods`(HEAD, OPTIONS, POST, PUT, GET, DELETE, PATCH))
      }
    }
  }
}
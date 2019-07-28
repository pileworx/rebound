package io.pileworx.rebound

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

  "Integration" should {
    "create a mock" in {

    }

    "retrieve the mock" in {

    }
  }
}
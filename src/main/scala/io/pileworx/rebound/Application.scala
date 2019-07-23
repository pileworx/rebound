package io.pileworx.rebound

import akka.http.scaladsl.Http
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import io.pileworx.rebound.application.ReboundService
import io.pileworx.rebound.common.akka.AkkaImplicits
import io.pileworx.rebound.common.akka.http.Cors
import io.pileworx.rebound.common.velocity.TemplateEngine
import io.pileworx.rebound.domain.MockRepository
import io.pileworx.rebound.port.primary.rest.{MockRoutes, ReboundRoutes}

import scala.concurrent.duration.Duration
import scala.concurrent.{Await, Future}
import scala.language.postfixOps
import scala.util.{Failure, Success}

object Application extends App with AkkaImplicits with Cors {

  val httpPort = if(sys.env.contains("HTTP_PORT")) sys.env("HTTP_PORT").asInstanceOf[Int] else 8585
  val engine = new TemplateEngine
  val repository = new MockRepository
  val service = new ReboundService(repository, engine)
  val routes: Route = new MockRoutes(service).routes
  val serverBinding: Future[Http.ServerBinding] = Http().bindAndHandle(routes, "0.0.0.0", httpPort)

  serverBinding.onComplete {
    case Success(bound) =>
      println(s"Server online at http://${bound.localAddress.getHostString}:${bound.localAddress.getPort}/")
    case Failure(e) =>
      Console.err.println("Server could not start!")
      e.printStackTrace()
      system.terminate()
  }

  Await.result(system.whenTerminated, Duration.Inf)
}
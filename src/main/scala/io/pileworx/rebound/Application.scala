package io.pileworx.rebound

import akka.http.scaladsl.Http
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import ch.qos.logback.classic.{Level, Logger}
import io.pileworx.rebound.application.ReboundService
import io.pileworx.rebound.application.assembler.MockAssembler
import io.pileworx.rebound.common.akka.AkkaImplicits
import io.pileworx.rebound.common.akka.http.Cors
import io.pileworx.rebound.common.velocity.TemplateEngine
import io.pileworx.rebound.domain.MockRepository
import io.pileworx.rebound.port.primary.rest.{MockRoutes, ReboundRoutes}
import org.apache.velocity.app.Velocity
import org.apache.velocity.runtime.RuntimeConstants
import org.slf4j.LoggerFactory

import scala.concurrent.duration.Duration
import scala.concurrent.{Await, Future}
import scala.language.postfixOps
import scala.util.{Failure, Success}

object Application extends App with AkkaImplicits with Cors {

  val root: Logger = LoggerFactory.getLogger(classOf[TemplateEngine]).asInstanceOf[Logger]
  root.setLevel(Level.ERROR)
  Velocity.setProperty(RuntimeConstants.RUNTIME_LOG_INSTANCE, root)

  val httpPort = if(sys.env.contains("HTTP_PORT")) sys.env("HTTP_PORT").asInstanceOf[Int] else 8585
  val engine = new TemplateEngine
  val repository = new MockRepository
  val assembler = new MockAssembler
  val service = new ReboundService(repository, engine, assembler)
  val routes: Route = cors { new MockRoutes(service).routes ~ Route.seal(new ReboundRoutes(service).routes) }
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
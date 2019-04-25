package io.pileworx.rebound

import akka.actor.ActorSystem
import akka.stream.ActorMaterializer
import akka.util.Timeout
import com.typesafe.config.{Config, ConfigFactory}

import scala.concurrent.duration._
import scala.concurrent.ExecutionContext
import scala.language.postfixOps

trait AkkaImplicits {
  implicit val system: ActorSystem = ActorSystem("rebound-actor-system")
  implicit val materializer: ActorMaterializer = ActorMaterializer()
  implicit val executionContext: ExecutionContext = system.dispatcher
  implicit val timeout: Timeout = Timeout(30 seconds)
  val config: Config = ConfigFactory.load()
}

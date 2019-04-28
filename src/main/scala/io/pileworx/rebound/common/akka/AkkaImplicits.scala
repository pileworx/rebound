package io.pileworx.rebound.common.akka

import akka.actor.ActorSystem
import akka.stream.ActorMaterializer
import akka.util.Timeout
import com.typesafe.config.{Config, ConfigFactory}
import scala.language.postfixOps
import scala.concurrent.ExecutionContext
import scala.concurrent.duration._

trait AkkaImplicits {
  implicit val system: ActorSystem = ActorSystem("rebound-actor-system")
  implicit val materializer: ActorMaterializer = ActorMaterializer()
  implicit val executionContext: ExecutionContext = system.dispatcher
  implicit val timeout: Timeout = Timeout(30 seconds)
  val config: Config = ConfigFactory.load()
}

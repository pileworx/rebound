package io.pileworx.rebound.common.akka.http.stomp

import io.pileworx.rebound.common.akka.http.stomp.StompCommand.StompCommand
import org.parboiled2._
import shapeless.HList
import shapeless.ops.hlist.ToList

import scala.util.{Failure, Success}

trait FrameParser[T] {
}

class TextFrameParser(val input: ParserInput) extends Parser with FrameParser[String] {

  def parse() = {
    val parsed = frameParse.run() match {
      case Success(result) =>
        result
      case Failure(e: ParseError) => sys.error(formatError(e, new ErrorFormatter(showTraces = true)))
      case Failure(e) => throw e
    }

    val parts = flatten(parsed)

    StompFrame(
      parts(0).asInstanceOf[StompCommand],
      parts(1).asInstanceOf[Option[Seq[StompHeader]]],
      parts(2).asInstanceOf[String])

  }

  private def flatten[H <: HList](h: H)(implicit ev: ToList[H, Any]) = h.toList[Any]

  private def frameParse = rule {
    command ~ newLine ~ headers.? ~ newLine ~ newLine ~ body
  }

  private def command: Rule1[StompCommand] = rule {
    capture("STOMP" | "CONNECT" | "SEND" | "SUBSCRIBE" | "UNSUBSCRIBE" | "BEGIN" | "COMMIT" | "ABORT" | "ACK" | "NACK" | "DISCONNECT") ~> ((cmd: String) => StompCommand.withName(cmd))
  }

  private def headers: Rule1[Seq[StompHeader]] = rule {
    header.+(newLine)
  }

  private def header: Rule1[StompHeader] =  rule {
    capture((CharPredicate.Alpha | "-").+) ~ ":" ~ capture(noneOf("\r\n").+) ~> StompHeader
  }

  private def newLine = rule {
    "\r\n" | "\n"
  }

  private def body = rule {
    capture(CharPredicate.Printable.+)
  }
}

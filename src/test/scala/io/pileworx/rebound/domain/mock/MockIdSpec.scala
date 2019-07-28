package io.pileworx.rebound.domain.mock

import akka.http.scaladsl.model.headers.{Accept, Cookie}
import akka.http.scaladsl.model.{HttpHeader, HttpRequest, MediaRange, MediaTypes}
import io.pileworx.rebound.domain.command.{DefineMockCmd, DefineRequestCmd, When}
import org.scalamock.scalatest.MockFactory
import org.scalatest.{Matchers, WordSpec}

import scala.collection.immutable

class MockIdSpec extends WordSpec with Matchers with MockFactory {

  "MockId apply with DefineMockCmd" should {

    val body =
      """
        |{
        | "foo":"bar"
        |}
        |""".stripMargin

    val cmd = DefineMockCmd(
      "Test",
      When(
        DefineRequestCmd(
          Put(),
          "/foo",
          None,
          Some(Seq(
            Header("Accept", "application/json"),
            Header("Cookie", "a cookie"))),
          Some(body))
      ),
      List())

    val id = MockId(cmd)

    "filter out unsupported headers" in {
      id.headers.get.size should be(1)
    }

    "reformat body without whitespace" in {
      id.body.get should be("""{"foo":"bar"}""")
    }
  }

  "MockId apply with HttpRequest and entity" should {

    val headers: immutable.Seq[HttpHeader] = immutable.Seq(Accept(MediaRange.apply(MediaTypes.`application/json`)), Cookie("key", "value"))
    val request = HttpRequest(headers = headers)
    val body =
      """
        |{
        | "foo":"bar"
        |}
        |""".stripMargin
    val id = MockId(request, Some(body))

    "filter out unsupported headers" in {
      id.headers.get.size should be(1)
    }

    "reformat body without whitespace" in {
      id.body.get should be("""{"foo":"bar"}""")
    }
  }
}

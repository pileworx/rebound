package io.pileworx.rebound.domain.mock

import org.scalatest.{Matchers, WordSpec}
import Method._

class MethodSpec extends WordSpec with Matchers {

  "Method apply" should {

    "return Get if value is GET" in {
      val method = Method(GET)

      method should be(Get(GET))
    }

    "return Put if value is PUT" in {
      val method = Method(PUT)

      method should be(Put(PUT))
    }

    "return Post if value is POST" in {
      val method = Method(POST)

      method should be(Post(POST))
    }

    "return Head if value is HEAD" in {
      val method = Method(HEAD)

      method should be(Head(HEAD))
    }

    "return Patch if value is PATCH" in {
      val method = Method(PATCH)

      method should be(Patch(PATCH))
    }

    "return Delete if value is DELETE" in {
      val method = Method(DELETE)

      method should be(Delete(DELETE))
    }

    "return Options if value is OPTIONS" in {
      val method = Method(OPTIONS)

      method should be(Options(OPTIONS))
    }
  }

  "Method value" should {

    "return the string value" in {
      val method = Method(OPTIONS)

      method.value should be(OPTIONS)
    }
  }
}

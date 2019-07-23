package io.pileworx.rebound.domain

import io.pileworx.rebound.domain.mock.{Get, MockId, Put}
import org.scalamock.scalatest.MockFactory
import org.scalatest.{Matchers, WordSpec}

import scala.collection.mutable

class MockRepositorySpec extends WordSpec with Matchers with MockFactory {

  val repository = new MockRepository
  val id = MockId(Get(), "", None, None, None)
  val mock = Mock(id, "Test", mutable.Seq())

  "MockRepository findById" should {

    "return some mock if id is matched" in {
      repository.save(mock)
      val result = repository.findById(id)

      result.get should be theSameInstanceAs mock
    }

    "return None if id is not matched" in {
      repository.save(mock)
      val result = repository.findById(MockId(Put(), "", None, None, None))

      result should be(None)
    }
  }

  "MockRepository reset" should {

    "remove all mocks" in {
      repository.save(mock)
      val result = repository.findById(id)
      repository.reset()
      val resultNone = repository.findById(id)

      result.get should be theSameInstanceAs mock
      resultNone should be(None)
    }
  }
}

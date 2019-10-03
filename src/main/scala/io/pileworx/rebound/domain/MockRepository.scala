package io.pileworx.rebound.domain

import io.pileworx.rebound.domain.mock.MockId

import scala.collection.mutable

object MockRepository {
  private val storage: mutable.Map[MockId, Mock] = mutable.Map[MockId, Mock]()
}

class MockRepository {
  def findAll(): Map[MockId, Mock] = collection.immutable.Map(MockRepository.storage.toSeq: _*)
  def findById(id: MockId): Option[Mock] = if(MockRepository.storage.contains(id)) Some(MockRepository.storage(id)) else None
  def save(mock: Mock): Unit = MockRepository.storage.put(mock.id, mock)
  def reset(): Unit = MockRepository.storage.clear()
}


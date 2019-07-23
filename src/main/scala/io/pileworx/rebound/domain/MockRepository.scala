package io.pileworx.rebound.domain

import io.pileworx.rebound.domain.mock.MockId

import scala.collection.mutable

class MockRepository {

  private val storage: mutable.Map[MockId, Mock] = mutable.Map[MockId, Mock]()

  def findById(id: MockId): Option[Mock] = if(storage.contains(id)) Some(storage(id)) else None

  def save(mock: Mock): Unit = storage.put(mock.id, mock)

  def reset(): Unit = storage.clear()
}
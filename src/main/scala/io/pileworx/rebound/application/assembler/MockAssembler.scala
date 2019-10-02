package io.pileworx.rebound.application.assembler

import io.pileworx.rebound.application.dto.{MockDto, RequestDto, ResponseDto}
import io.pileworx.rebound.domain.Mock
import io.pileworx.rebound.domain.mock.MockId

class MockAssembler {
  def toDtos(mocks: Map[MockId, Mock]): List[MockDto] = mocks.map(m => toDto(m._1, m._2)).toList
  def toDto(id: MockId, mock:Mock): MockDto = MockDto(
    mock.scenario,
    RequestDto(id.method, id.path, id.query, id.headers, id.body),
    mock.responses.map(r => ResponseDto(r.status, r.headers, r.body, r.values)).toList
  )
}
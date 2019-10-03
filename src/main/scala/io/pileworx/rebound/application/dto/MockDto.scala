package io.pileworx.rebound.application.dto

import io.pileworx.rebound.domain.mock.{Header, Method}

case class MockDto(scenario: String,
                   when: RequestDto,
                   `then`: List[ResponseDto])

case class RequestDto(method: Method,
                      path: String,
                      query: Option[String],
                      headers: Option[Seq[Header]],
                      body: Option[String])

case class ResponseDto(status: Int,
                       headers: Option[Seq[Header]],
                       body: Option[String],
                       values: Option[Map[String, String]])

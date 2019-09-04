package io.pileworx.rebound.domain.command

import io.pileworx.rebound.domain.mock.{Header, Method}

case class DefineMockCmd(scenario: String,
                         when: When,
                         `then`: List[DefineResponseCmd])

case class When(request: DefineRequestCmd)

case class DefineRequestCmd(method: Method,
                            path: String,
                            query: Option[String],
                            headers: Option[Seq[Header]],
                            body: Option[String])

case class DefineResponseCmd(status: Int,
                             headers: Option[Seq[Header]],
                             body: Option[String],
                             values: Option[Map[String, String]])
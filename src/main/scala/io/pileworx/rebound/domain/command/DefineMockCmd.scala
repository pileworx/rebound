package io.pileworx.rebound.domain.command

import io.pileworx.rebound.domain.{Header, Method, Response}


case class DefineMockCmd(scenario: String,
                         when: When,
                         `then`: Then)

case class When(request: DefineRequestCmd)
case class Then(seq: Seq[Response])

case class DefineRequestCmd(method: Method,
                            path: String,
                            query: Option[String],
                            headers: Option[Seq[Header]],
                            body: Option[String])
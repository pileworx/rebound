package io.pileworx.rebound.application

import scala.collection.immutable.Map

case class DefineMockCmd(method: String,
                         path: String,
                         qs: Option[String],
                         status: Int,
                         response: Option[String],
                         contentType: String,
                         values: Option[Map[String, String]])
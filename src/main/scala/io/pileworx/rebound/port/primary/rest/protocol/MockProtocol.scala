package io.pileworx.rebound.port.primary.rest.protocol

import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import io.pileworx.rebound.application.dto.{MockDto, RequestDto, ResponseDto}
import io.pileworx.rebound.domain.command.{DefineMockCmd, DefineRequestCmd, DefineResponseCmd, When}
import io.pileworx.rebound.domain.mock.{Header, Method}
import spray.json._

trait MockProtocol extends SprayJsonSupport with DefaultJsonProtocol  {
  implicit val methodFormat: RootJsonFormat[Method] = new RootJsonFormat[Method] {
    def read(value: JsValue): Method = Method(value.asInstanceOf[JsString].value)
    def write(method: Method): JsValue = JsString(method.value)
  }
  val headerApply: (String, String) => Header = Header.apply
  implicit val headerFormat: JsonFormat[Header] = jsonFormat2(headerApply)
  implicit val responseFormat: JsonFormat[DefineResponseCmd] = jsonFormat4(DefineResponseCmd)
  implicit val defineRequestCmd: JsonFormat[DefineRequestCmd] = jsonFormat5(DefineRequestCmd)
  implicit val whenFormat: JsonFormat[When] = jsonFormat1(When)
  implicit val defineMockCmdFormat: RootJsonFormat[DefineMockCmd] = jsonFormat3(DefineMockCmd)
  implicit val requestDtoFormat: JsonFormat[RequestDto] = jsonFormat5(RequestDto)
  implicit val responseDtoFormat: JsonFormat[ResponseDto] = jsonFormat4(ResponseDto)
  implicit val mockDtoFormat: RootJsonFormat[MockDto] = jsonFormat3(MockDto)
}

package io.pileworx.rebound.storage

import org.scalatest.{Matchers, WordSpec}

class TemplateEngineSpec extends WordSpec with Matchers {

  private val template =
    """
      |{
      | "propertyName":"$myValue"
      |}
    """.stripMargin
  private val engine = new TemplateEngine
  private val replacedValue = "replaced value"

  "Template Engine" should {
    "process the template with supplied variables" in {
      val values = Map("myValue" -> replacedValue)
      val result = engine.process(template, values)

      result should include(replacedValue)
    }
  }
}
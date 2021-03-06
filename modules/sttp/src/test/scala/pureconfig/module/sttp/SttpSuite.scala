package pureconfig.module.sttp

import com.softwaremill.sttp._
import com.typesafe.config.ConfigFactory
import pureconfig.BaseSuite
import pureconfig.error.{ CannotConvert, ConfigReaderFailures, ConvertFailure }
import pureconfig.generic.auto._
import pureconfig.syntax._

class SttpSuite extends BaseSuite {

  case class AppConfig(uri: Uri)

  behavior of "sttp module"

  it should "read uri" in {
    val config = ConfigFactory.parseString("""{uri = "https://sttp.readthedocs.io"}""")

    config.to[AppConfig].right.value shouldBe AppConfig(uri"https://sttp.readthedocs.io")
  }

  it should "handle error when reading uri" in {
    val config = ConfigFactory.parseString("""{uri = "https!!://wrong.io"}""")

    val failure =
      ConvertFailure(
        reason = CannotConvert(
          value = "https!!://wrong.io",
          toType = "com.softwaremill.sttp.Uri",
          because = "requirement failed: Scheme can only contain alphanumeric characters, +, - and ."),
        location = None,
        path = "uri")

    config.to[AppConfig].left.value shouldBe ConfigReaderFailures(failure)
  }

}

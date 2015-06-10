package eu.semberal.parsercombinatorsexample

import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import org.scalatest.FunSuite
import eu.semberal.parsercombinatorsexample.IPParser

@RunWith(classOf[JUnitRunner])
class IPTest extends FunSuite {

  test("Test if IP parsing using regexps and parser combinator produces the same result") {

    val parser = new IPParser
    val ips = List("1.1.1.1", "1.2.3.256", "17.jko.32.", "255.255.255.255", "....", "13.14.35.179", "", "    ", ".", "112 .32.15.17", "112.32.14.17", "112.32.14.1722", "112.32.14.172 ")


    ips.foreach {
      x => expect(parser.parseUsingRegexp(x))(parser.parseUsingParserCombinator(x))
    }
  }
}

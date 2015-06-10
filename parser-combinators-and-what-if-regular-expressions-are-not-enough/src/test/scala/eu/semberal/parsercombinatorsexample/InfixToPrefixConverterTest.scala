package eu.semberal.parsercombinatorsexample

import org.scalatest.FunSuite
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
class InfixToPrefixConverterTest extends FunSuite {

  test("Test conversion from infix to prefix notation") {

    val converter = new InfixToPrefixConverter

    val tests = List(
      ("not true", "(not #t)"),
      ("not (not true)", "(not (not #t))"),
      ("true and false", "(and #t #f)"),
      ("true and not (false or true)", "(and #t (not (or #f #t)))"),
      ("true", "#t"),
      ("false", "#f"),
      ("not (true and false)", "(not (and #t #f))"),
      ("true or (not false)", "(or #t (not #f))")
    )

    tests.foreach {
      case (x, y) => expect(y)(converter.convert(x))
    }
  }

}

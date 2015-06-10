package eu.semberal.parsercombinatorsexample

import util.parsing.combinator.RegexParsers

class IPParser extends RegexParsers {
  override val skipWhitespace = false


  def parseUsingRegexp(input: String): Option[(Int, Int, Int, Int)] = {
    val IP = """\b(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\.(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\.(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\.(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\b""".r
    input match {
      case IP(a, b, c, d) => Some(a.toInt, b.toInt, c.toInt, d.toInt)
      case _ => None
    }
  }


  def parseUsingParserCombinator(input: String): Option[(Int, Int, Int, Int)] = {
    def dot: Parser[Any] = "."
    def ipGroup: Parser[Int] = "25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?".r ^^ (_.toInt)

    def ip: Parser[(Int, Int, Int, Int)] =
      (ipGroup <~ dot) ~ (ipGroup <~ dot) ~
        (ipGroup <~ dot) ~ ipGroup ^^ {
      case (a ~ b ~ c ~ d) => (a, b, c, d)
    }

    parseAll(ip, input) match {
      case Success(r, _) => Some(r)
      case _ => None
    }
  }


}
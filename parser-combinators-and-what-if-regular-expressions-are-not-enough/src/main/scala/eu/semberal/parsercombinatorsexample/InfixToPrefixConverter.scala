package eu.semberal.parsercombinatorsexample

import util.parsing.combinator.RegexParsers

sealed trait Expr

case class Atom(value: Boolean) extends Expr

case class Not(expr: Expr) extends Expr

case class And(left: Expr, right: Expr) extends Expr

case class Or(left: Expr, right: Expr) extends Expr

case class Paren(value: Expr) extends Expr


class InfixToPrefixConverter extends RegexParsers {

  override val skipWhitespace = false

  def convert(input: String) = {
    def doConvertToInfix(expr: Expr): String = expr match {
      case x: And =>
        "(and " + doConvertToInfix(x.left) + " " + doConvertToInfix(x.right) + ")"
      case x: Or =>
        "(or " + doConvertToInfix(x.left) + " " + doConvertToInfix(x.right) + ")"
      case x: Not =>
        "(not " + doConvertToInfix(x.expr) + ")"
      case x: Atom => if (x.value) "#t" else "#f"
      case x: Paren => doConvertToInfix(x.value)
    }

    def parseExpression(s: String): Expr = parseAll(expr, s) match {
      case x@Success(res, _) => res
      case x@Failure(msg, _) => throw new Exception(msg)
    }

    val ast = parseExpression(input)
    doConvertToInfix(ast)
  }


  private def expr: Parser[Expr] =
    (elem <~ whiteSpace ~ "and" ~ whiteSpace) ~ elem ^^ {
      case x ~ y => And(x, y)
    } |
      (elem <~ whiteSpace ~ "or" ~ whiteSpace) ~ elem ^^ {
        case x ~ y => Or(x, y)
      } |
      elem

  private def elem: Parser[Expr] = "not" ~> whiteSpace ~> atom ^^ {
    Not(_)
  } | atom

  private def atom: Parser[Expr] =
    ("true" | "false") ^^ (x => Atom(value = x.toBoolean)) |
      "(" ~> expr <~ ")"
}
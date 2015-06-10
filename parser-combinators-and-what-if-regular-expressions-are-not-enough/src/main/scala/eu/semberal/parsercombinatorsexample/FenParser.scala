package eu.semberal.parsercombinatorsexample

import util.parsing.combinator.RegexParsers

sealed abstract class Piece {
  val white: Boolean
}

object Piece {
  def apply(str: String): Piece = str match {
    case "p" => new Pawn(false)
    case "P" => new Pawn
    case "r" => new Rook(false)
    case "R" => new Rook
    case "n" => new Knight(false)
    case "N" => new Knight
    case "b" => new Bishop(false)
    case "B" => new Bishop
    case "q" => new Queen(false)
    case "Q" => new Queen
    case "k" => new King(false)
    case "K" => new King
    case _ => throw new IllegalArgumentException("Passed incorrect piece representation")
  }
}

case class Pawn(white: Boolean = true) extends Piece

case class Rook(white: Boolean = true) extends Piece

case class Knight(white: Boolean = true) extends Piece

case class Bishop(white: Boolean = true) extends Piece

case class Queen(white: Boolean = true) extends Piece

case class King(white: Boolean = true) extends Piece


sealed abstract class Castling {
  val white: Boolean
}

object Castling {
  def apply(str: String): Castling = str match {
    case "k" => new KingsideCastling(false)
    case "K" => new KingsideCastling
    case "q" => new QueensideCastling(false)
    case "Q" => new QueensideCastling
    case _ => throw new IllegalArgumentException("Passed incorrect castling representation")
  }
}

case class KingsideCastling(white: Boolean = true) extends Castling

case class QueensideCastling(white: Boolean = true) extends Castling


class GameState private(val board: List[Option[Piece]],
                        val whiteOnTurn: Boolean,
                        val castlingAvailability: Set[Castling],
                        val enPassantSquare: Option[Int],
                        val halfMoveClock: Int,
                        val fullmoveNumber: Int)


object GameState {
  def apply(fen: String): Option[GameState] = {
    val parser = new RegexParsers {
      override val skipWhitespace = false

      val activeColorParser: Parser[Boolean] = ("w" | "b") ^^ (_ == "w")

      val castlingParser: Parser[Set[Castling]] =
        "-" ^^ (_ => Set[Castling]()) | rep("k" | "K" | "q" | "Q") ^^
          (_.map(x => Castling(x)) toSet)

      val enpassantSquareParser: Parser[Option[Int]] =
        "-" ^^ (_ => None) |
          ("a" | "b" | "c" | "d" | "e" | "f" | "g" | "h") ~
            ("1" | "2" | "3" | "4" | "5" | "6" | "7" | "8") ^^ {
            case x ~ y => Some(8 * (y.head - '1') + (x.head - 'a'))
          }

      val intParser: Parser[Int] = """\d+""".r ^^ (_.toInt)


      val occupiedSquaresParser: Parser[List[Option[Piece]]] =
        ("p" | "P" | "r" | "R" | "n" | "N" | "b" | "B" | "q" | "Q" | "k" | "K") ^^
          (x => List(Some(Piece(x))))

      val emptySquaresParser: Parser[List[Option[Piece]]] =
        ("1" | "2" | "3" | "4" | "5" | "6" | "7" | "8") ^^
          (n => List.fill(n.toInt)(None))

      val rankParser: Parser[List[Option[Piece]]] =
        rep(occupiedSquaresParser | emptySquaresParser) ^^ (_.flatMap(x => x))

      val boardParser: Parser[List[Option[Piece]]] = repsep(rankParser, "/") ^^
        (_.reverse.flatMap(x => x))


      val fenParser: Parser[GameState] =
        (boardParser <~ whiteSpace) ~ (activeColorParser <~ whiteSpace) ~
          (castlingParser <~ whiteSpace) ~ (enpassantSquareParser <~ whiteSpace) ~
          (intParser <~ whiteSpace) ~ intParser ^^ {
          case a ~ b ~ c ~ d ~ e ~ f => new GameState(a, b, c, d, e, f)
        }

      def doParse(fen: String): Option[GameState] = parseAll(fenParser, fen) match {
        case Success(result, _) => Some(result)
        case Failure(msg, _) => throw new Exception(msg)

      }
    }
    parser.doParse(fen)
  }
}
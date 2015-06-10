package eu.semberal.parsercombinatorsexample

import org.scalatest.FunSuite
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner

/**
 * @author lukas.sembera
 */
@RunWith(classOf[JUnitRunner])
class GameStateParsingTest extends FunSuite {

  test("Test board initialization from FEN string") {
    val x = GameState("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 1 1").get
    expect(Set(KingsideCastling(white = true), KingsideCastling(white = false), QueensideCastling(white = true), QueensideCastling(white = false)))(x.castlingAvailability)
    assert(x.whiteOnTurn)
    expect(1)(x.halfMoveClock)
    expect(1)(x.fullmoveNumber)
    expect(None)(x.enPassantSquare)
    for (i <- 16 to 47)
      (expect(None)(x.board(i)))

    expect(Some(Rook(white = true)))(x.board(0))
    expect(Some(Knight(white = true)))(x.board(1))
    expect(Some(Bishop(white = true)))(x.board(2))
    expect(Some(Queen(white = true)))(x.board(3))
    expect(Some(King(white = true)))(x.board(4))

    expect(Some(Rook(white = false)))(x.board(63))
    expect(Some(Knight(white = false)))(x.board(62))
    expect(Some(Bishop(white = false)))(x.board(61))
    expect(Some(King(white = false)))(x.board(60))
    expect(Some(Queen(white = false)))(x.board(59))
  }

  test("Test en passant square parsing") {
    val g1 = GameState("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq c3 1 1").get
    expect(18)(g1.enPassantSquare.get)

    val g2 = GameState("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq h8 1 1").get
    expect(63)(g2.enPassantSquare.get)

    val g3 = GameState("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq g1 1 1").get
    expect(6)(g3.enPassantSquare.get)

  }

  test("Test no castlings available parsing") {
    val x = GameState("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w - - 1 1").get

    expect(Set[Castling]())(x.castlingAvailability)
  }

}

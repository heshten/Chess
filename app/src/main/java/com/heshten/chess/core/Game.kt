package com.heshten.chess.core

import com.heshten.chess.core.board.ChessBoard
import com.heshten.chess.core.logic.MoveChecker
import com.heshten.chess.core.logic.TakeChecker
import com.heshten.chess.core.models.BoardPosition
import com.heshten.chess.core.models.pieces.Piece
import com.heshten.chess.core.validator.SideMoveValidator

class Game(
  val chessBoard: ChessBoard,
  private val movesCheckerFacade: MoveChecker,
  private val takesCheckerFacade: TakeChecker,
  private val sideMoveValidator: SideMoveValidator,
  private val redrawBoard: () -> Unit
) {

  fun selectPiece(piece: Piece) {
    if (sideMoveValidator.getCurrentSide() == piece.pieceSide) {
      chessBoard.setPossibleMoves(getPossibleMovesForPiece(piece))
      chessBoard.selectPiece(piece)
      redrawBoard()
    }
  }

  fun moveSelectedPieceToPosition(boardPosition: BoardPosition) {
    if (chessBoard.hasPieceAtPosition(boardPosition)) {
      //take piece
      chessBoard.removePieceAtPosition(boardPosition)
    }
    chessBoard.moveSelectedPieceToPosition(boardPosition)
    chessBoard.clearPossibleMovesPositions()
    sideMoveValidator.changeSide()
    redrawBoard()
  }

  private fun getPossibleMovesForPiece(piece: Piece): Set<BoardPosition> {
    val possibleMoves = mutableSetOf<BoardPosition>()
    possibleMoves.addAll(movesCheckerFacade.getPossibleMoves(piece))
    possibleMoves.addAll(takesCheckerFacade.getPossibleTakes(piece))
    return possibleMoves
  }

  private fun redrawBoard() {
    redrawBoard.invoke()
  }
}

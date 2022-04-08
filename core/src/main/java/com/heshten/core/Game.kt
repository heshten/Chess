package com.heshten.core

import com.heshten.core.board.ChessBoard
import com.heshten.core.logic.MoveChecker
import com.heshten.core.logic.TakeChecker
import com.heshten.core.models.BoardPosition
import com.heshten.core.models.pieces.Piece
import com.heshten.core.validator.SideMoveValidator

class Game(
  private val chessBoard: ChessBoard,
  private val movesCheckerFacade: MoveChecker,
  private val takesCheckerFacade: TakeChecker,
  private val sideMoveValidator: SideMoveValidator,
  private val redrawBoard: (ChessBoard) -> Unit
) {

  init {
    redrawBoard()
  }

  fun onPositionTouched(boardPosition: BoardPosition) {
    val pieceAtPosition = chessBoard.getPieceAtPosition(boardPosition)
    if (pieceAtPosition == null) {
      if (chessBoard.isPossibleMoveTo(boardPosition)) {
        moveSelectedPieceToPosition(boardPosition)
      }
    } else {
      if (chessBoard.isPossibleMoveTo(boardPosition)) {
        moveSelectedPieceToPosition(boardPosition)
      } else {
        selectPiece(pieceAtPosition)
      }
    }
  }

  private fun selectPiece(piece: Piece) {
    if (sideMoveValidator.getCurrentSide() == piece.pieceSide) {
      chessBoard.setPossibleMoves(getPossibleMovesForPiece(piece))
      chessBoard.selectPiece(piece)
      redrawBoard()
    }
  }

  private fun moveSelectedPieceToPosition(boardPosition: BoardPosition) {
    if (chessBoard.hasPieceAtPosition(boardPosition)) {
      //take piece
      chessBoard.removePieceAtPosition(boardPosition)
    }
    chessBoard.moveSelectedPieceToPosition(boardPosition)
    chessBoard.clearPossibleMovesPositions()
    sideMoveValidator.changeSide()
    redrawBoard()
  }

  fun getPossibleMovesForPiece(piece: Piece): Set<BoardPosition> {
    val possibleMoves = mutableSetOf<BoardPosition>()
    possibleMoves.addAll(movesCheckerFacade.getPossibleMoves(piece))
    possibleMoves.addAll(takesCheckerFacade.getPossibleTakes(piece))
    return possibleMoves
  }

  private fun redrawBoard() {
    redrawBoard.invoke(chessBoard)
  }
}

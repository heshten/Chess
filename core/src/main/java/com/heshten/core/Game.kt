package com.heshten.core

import com.heshten.core.board.ChessBoard
import com.heshten.core.logic.MoveChecker
import com.heshten.core.logic.TakeChecker
import com.heshten.core.models.BoardPosition
import com.heshten.core.models.PieceSide
import com.heshten.core.models.opposite
import com.heshten.core.models.pieces.King
import com.heshten.core.models.pieces.Piece
import com.heshten.core.validator.SideMoveValidator

class Game(
  private val chessBoard: ChessBoard,
  private val movesCheckerFacade: MoveChecker,
  private val takesCheckerFacade: TakeChecker,
  private val sideMoveValidator: SideMoveValidator,
  private val redrawBoard: (ChessBoard) -> Unit,
  private val gameFinished: (GameResult) -> Unit
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

  fun calculatePossibleMovesForPiece(piece: Piece): Set<BoardPosition> {
    val possibleMoves = mutableSetOf<BoardPosition>()
    possibleMoves.addAll(movesCheckerFacade.getPossibleMoves(piece))
    possibleMoves.addAll(takesCheckerFacade.getPossibleTakes(piece))
    return possibleMoves
  }

  private fun selectPiece(piece: Piece) {
    if (sideMoveValidator.getCurrentSide() == piece.pieceSide) {
      chessBoard.setPossibleMoves(calculatePossibleMovesForPiece(piece))
      chessBoard.selectPiece(piece)
      redrawBoard()
    }
  }

  private fun moveSelectedPieceToPosition(boardPosition: BoardPosition) {
    if (chessBoard.hasPieceAtPosition(boardPosition)) {
      val takingPiece = requireNotNull(chessBoard.getPieceAtPosition(boardPosition))
      if (takingPiece is King) {
        val winnerSide = takingPiece.pieceSide.opposite()
        chessBoard.removePieceAtPosition(boardPosition)
        chessBoard.moveSelectedPieceToPosition(boardPosition)
        chessBoard.clearPossibleMovesPositions()
        redrawBoard()
        gameFinished.invoke(GameResult(winnerSide))
        return
      }
      chessBoard.removePieceAtPosition(boardPosition)
    }
    chessBoard.moveSelectedPieceToPosition(boardPosition)
    chessBoard.clearPossibleMovesPositions()
    sideMoveValidator.changeSide()
    redrawBoard()
  }

  private fun redrawBoard() {
    redrawBoard.invoke(chessBoard)
  }

  data class GameResult(
    val winner: PieceSide
  )
}

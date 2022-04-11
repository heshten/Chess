package com.heshten.core

import com.heshten.core.board.ChessBoard
import com.heshten.core.logic.PossibleMovesCalculator
import com.heshten.core.models.BoardPosition
import com.heshten.core.models.PieceSide
import com.heshten.core.models.opposite
import com.heshten.core.models.pieces.King
import com.heshten.core.models.pieces.Piece
import com.heshten.core.validator.SideMoveValidator

class Game(
  private val chessBoard: ChessBoard,
  private val possibleMovesCalculator: PossibleMovesCalculator,
  private val sideMoveValidator: SideMoveValidator,
  private val redrawBoard: (Set<Piece>, Set<BoardPosition>) -> Unit,
  private val gameFinished: (GameResult) -> Unit
) {

  init {
    redrawBoardInternal()
  }

  fun onPositionTouched(boardPosition: BoardPosition) {
    val pieceAtPosition = chessBoard.getPieceAtPosition(boardPosition)
    if (chessBoard.isPossibleMoveTo(boardPosition)) {
      moveSelectedPieceToPosition(boardPosition)
    } else if (pieceAtPosition != null) {
      selectPiece(pieceAtPosition)
    }
  }

  private fun selectPiece(piece: Piece) {
    if (sideMoveValidator.getCurrentSide() == piece.pieceSide) {
      val possibleMoves = possibleMovesCalculator.calculatePossibleMovesForPiece(piece, chessBoard)
      chessBoard.setPossibleMoves(possibleMoves)
      chessBoard.selectPiece(piece)
      redrawBoardInternal()
    }
  }

  private fun moveSelectedPieceToPosition(boardPosition: BoardPosition) {
    chessBoard.moveSelectedPieceToPosition(boardPosition)
    chessBoard.setPossibleMoves(emptySet())
    val nextMoveSide = sideMoveValidator.getCurrentSide().opposite()
    val hasNextMoves = hasNextMoves(nextMoveSide)
    if (isCheck(nextMoveSide) && !hasNextMoves) {
      // check and mate
      gameFinished.invoke(GameResult.Winner(sideMoveValidator.getCurrentSide()))
      redrawBoardInternal()
    } else if (!hasNextMoves) {
      // draw
      gameFinished.invoke(GameResult.Draw)
      redrawBoardInternal()
    } else {
      sideMoveValidator.changeSide()
      redrawBoardInternal()
    }
  }

  private fun redrawBoardInternal() {
    redrawBoard.invoke(
      chessBoard.getAllPieces(),
      chessBoard.getPossibleMovesPositions()
    )
  }

  private fun isCheck(side: PieceSide): Boolean {
    val king = chessBoard.getAllPieces().first { it.pieceSide == side && it is King }
    return possibleMovesCalculator.isUnderAttack(king, chessBoard)
  }

  private fun hasNextMoves(side: PieceSide): Boolean {
    var possibleMoves = 0
    chessBoard.getAllPiecesForSide(side).forEach { piece ->
      possibleMoves += possibleMovesCalculator
        .calculatePossibleMovesForPiece(piece, chessBoard).size
    }
    return possibleMoves > 0
  }

  sealed class GameResult {
    object Draw : GameResult()
    data class Winner(val side: PieceSide) : GameResult()
  }
}

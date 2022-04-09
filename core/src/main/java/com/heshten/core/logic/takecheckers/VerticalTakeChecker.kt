package com.heshten.core.logic.takecheckers

import com.heshten.core.board.ChessBoard
import com.heshten.core.logic.PositionExcluder
import com.heshten.core.logic.TakeChecker
import com.heshten.core.models.BoardPosition
import com.heshten.core.models.pieces.Piece

class VerticalTakeChecker(private val chessBoard: ChessBoard) : TakeChecker {

  override fun getPossibleTakes(piece: Piece): Set<BoardPosition> {
    return getPossibleVerticalTakes(piece)
  }

  private fun getPossibleVerticalTakes(piece: Piece): Set<BoardPosition> {
    val possibleTakes = mutableSetOf<BoardPosition>()
    val possibleUpTakes = getPossibleVerticalUpTakes(piece)
    val possibleDownTakes = getPossibleVerticalDownTakes(piece)
    possibleTakes.addAll(possibleUpTakes)
    possibleTakes.addAll(possibleDownTakes)
    PositionExcluder.excludePositionsOutOfBoardInPlace(possibleTakes)
    return possibleTakes
  }

  private fun getPossibleVerticalUpTakes(piece: Piece): Set<BoardPosition> {
    val possibleMoves = mutableSetOf<BoardPosition>()
    val startRowPosition = piece.boardPosition.rowIndex - 1
    val columnIndex = piece.boardPosition.columnIndex
    (startRowPosition downTo 0).forEachIndexed { step, rowIndex ->
      if (step >= piece.maxTakeSteps()) {
        return possibleMoves
      }
      val nextVerticalPosition = BoardPosition(rowIndex, columnIndex)
      val nextPiece = chessBoard.getPieceAtPosition(nextVerticalPosition)
      if (nextPiece == null) {
        //move on
      } else {
        if (piece.direction != nextPiece.direction) {
          possibleMoves.add(nextVerticalPosition)
        }
        return possibleMoves
      }
    }
    return possibleMoves
  }

  private fun getPossibleVerticalDownTakes(piece: Piece): Set<BoardPosition> {
    val possibleTakes = mutableSetOf<BoardPosition>()
    val startRowPosition = piece.boardPosition.rowIndex + 1
    val columnIndex = piece.boardPosition.columnIndex
    (startRowPosition until 8).forEachIndexed { step, rowIndex ->
      if (step >= piece.maxTakeSteps()) {
        return possibleTakes
      }
      val nextVerticalPosition = BoardPosition(rowIndex, columnIndex)
      val nextPiece = chessBoard.getPieceAtPosition(nextVerticalPosition)
      if (nextPiece == null) {
        //move on
      } else {
        if (piece.direction != nextPiece.direction) {
          possibleTakes.add(nextVerticalPosition)
        }
        return possibleTakes
      }
    }
    return possibleTakes
  }
}

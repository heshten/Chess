package com.heshten.core.logic.takecheckers

import com.heshten.core.board.ChessBoard
import com.heshten.core.logic.PositionExclude
import com.heshten.core.logic.TakeChecker
import com.heshten.core.models.BoardPosition
import com.heshten.core.models.pieces.Piece

class VerticalTakeChecker : TakeChecker {

  override fun getPossibleTakes(piece: Piece, chessBoard: ChessBoard): Set<BoardPosition> {
    return getPossibleVerticalTakes(piece, chessBoard)
  }

  private fun getPossibleVerticalTakes(piece: Piece, chessBoard: ChessBoard): Set<BoardPosition> {
    val possibleTakes = mutableSetOf<BoardPosition>()
    val possibleUpTakes = getPossibleVerticalUpTakes(piece, chessBoard)
    val possibleDownTakes = getPossibleVerticalDownTakes(piece, chessBoard)
    possibleTakes.addAll(possibleUpTakes)
    possibleTakes.addAll(possibleDownTakes)
    PositionExclude.excludePositionsOutOfBoardInPlace(possibleTakes)
    return possibleTakes
  }

  private fun getPossibleVerticalUpTakes(piece: Piece, chessBoard: ChessBoard): Set<BoardPosition> {
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

  private fun getPossibleVerticalDownTakes(
    piece: Piece,
    chessBoard: ChessBoard
  ): Set<BoardPosition> {
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

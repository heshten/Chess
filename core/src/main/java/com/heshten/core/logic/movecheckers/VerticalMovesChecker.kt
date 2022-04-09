package com.heshten.core.logic.movecheckers

import com.heshten.core.board.ChessBoard
import com.heshten.core.logic.MoveChecker
import com.heshten.core.logic.PositionExclude
import com.heshten.core.models.BoardPosition
import com.heshten.core.models.Direction
import com.heshten.core.models.pieces.Piece

class VerticalMovesChecker : MoveChecker {

  override fun getPossibleMoves(piece: Piece, chessBoard: ChessBoard): Set<BoardPosition> {
    return getPossibleVerticalMoves(piece, chessBoard)
  }

  private fun getPossibleVerticalMoves(piece: Piece, chessBoard: ChessBoard): Set<BoardPosition> {
    val possibleMoves = mutableSetOf<BoardPosition>()
    val possibleUpMoves = getPossibleVerticalUpMoves(piece, chessBoard)
    val possibleDownMoves = getPossibleVerticalDownMoves(piece, chessBoard)
    if (piece.canMoveBehind()) {
      possibleMoves.addAll(possibleUpMoves)
      possibleMoves.addAll(possibleDownMoves)
    } else {
      //detect which direction is allowed to be calculated for curtain piece
      when (piece.direction) {
        Direction.UP -> {
          //from bottom to top allowed
          possibleMoves.addAll(possibleUpMoves)
        }
        Direction.DOWN -> {
          //from top to bottom allowed
          possibleMoves.addAll(possibleDownMoves)
        }
      }
    }
    PositionExclude.excludePositionsOutOfBoardInPlace(possibleMoves)
    return possibleMoves
  }

  private fun getPossibleVerticalUpMoves(piece: Piece, chessBoard: ChessBoard): Set<BoardPosition> {
    val possibleMoves = mutableSetOf<BoardPosition>()
    val startRowPosition = piece.boardPosition.rowIndex - 1
    val columnIndex = piece.boardPosition.columnIndex
    (startRowPosition downTo 0).forEachIndexed { step, rowIndex ->
      if (step >= piece.maxSteps()) {
        return possibleMoves
      }
      val nextVerticalPosition = BoardPosition(rowIndex, columnIndex)
      if (!chessBoard.hasPieceAtPosition(nextVerticalPosition)) {
        possibleMoves.add(nextVerticalPosition)
      } else {
        return possibleMoves
      }
    }
    return possibleMoves
  }

  private fun getPossibleVerticalDownMoves(
    piece: Piece,
    chessBoard: ChessBoard
  ): Set<BoardPosition> {
    val possibleMoves = mutableSetOf<BoardPosition>()
    val startRowPosition = piece.boardPosition.rowIndex + 1
    val columnIndex = piece.boardPosition.columnIndex
    (startRowPosition until 8).forEachIndexed { step, rowIndex ->
      if (step >= piece.maxSteps()) {
        return possibleMoves
      }
      val nextVerticalPosition = BoardPosition(rowIndex, columnIndex)
      if (!chessBoard.hasPieceAtPosition(nextVerticalPosition)) {
        possibleMoves.add(nextVerticalPosition)
      } else {
        return possibleMoves
      }
    }
    return possibleMoves
  }
}

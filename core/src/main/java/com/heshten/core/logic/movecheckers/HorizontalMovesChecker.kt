package com.heshten.core.logic.movecheckers

import com.heshten.core.board.ChessBoard
import com.heshten.core.logic.MoveChecker
import com.heshten.core.logic.PositionExcluder
import com.heshten.core.models.BoardPosition
import com.heshten.core.models.pieces.Piece

class HorizontalMovesChecker(private val chessBoard: ChessBoard) : MoveChecker {

  override fun getPossibleMoves(piece: Piece): Set<BoardPosition> {
    return getPossibleHorizontalMoves(piece)
  }

  private fun getPossibleHorizontalMoves(piece: Piece): Set<BoardPosition> {
    val possibleMoves = mutableSetOf<BoardPosition>()
    val rightSideMoves = getRightSidePossibleMoves(piece)
    val leftSideMoves = getLeftSidePossibleMoves(piece)
    possibleMoves.addAll(rightSideMoves)
    possibleMoves.addAll(leftSideMoves)
    PositionExcluder.excludePositionsOutOfBoardInPlace(possibleMoves)
    return possibleMoves
  }

  private fun getRightSidePossibleMoves(piece: Piece): Set<BoardPosition> {
    val possibleMoves = mutableSetOf<BoardPosition>()
    val rowIndex = piece.boardPosition.rowIndex
    val startColumnIndex = piece.boardPosition.columnIndex + 1
    (startColumnIndex until 8).forEachIndexed { step, columnIndex ->
      if (step >= piece.maxSteps()) {
        return possibleMoves
      }
      val nextBoardPosition = BoardPosition(rowIndex, columnIndex)
      if (!chessBoard.hasPieceAtPosition(nextBoardPosition)) {
        possibleMoves.add(nextBoardPosition)
      } else {
        return possibleMoves
      }
    }
    return possibleMoves
  }

  private fun getLeftSidePossibleMoves(piece: Piece): Set<BoardPosition> {
    val possibleMoves = mutableSetOf<BoardPosition>()
    val rowIndex = piece.boardPosition.rowIndex
    val startColumnIndex = piece.boardPosition.columnIndex - 1
    (startColumnIndex downTo 0).forEachIndexed { step, columnIndex ->
      if (step >= piece.maxSteps()) {
        return possibleMoves
      }
      val nextBoardPosition = BoardPosition(rowIndex, columnIndex)
      if (!chessBoard.hasPieceAtPosition(nextBoardPosition)) {
        possibleMoves.add(nextBoardPosition)
      } else {
        return possibleMoves
      }
    }
    return possibleMoves
  }
}

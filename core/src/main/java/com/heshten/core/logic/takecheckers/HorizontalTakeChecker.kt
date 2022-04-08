package com.heshten.core.logic.takecheckers

import com.heshten.core.board.ChessBoard
import com.heshten.core.logic.TakeChecker
import com.heshten.core.models.BoardPosition
import com.heshten.core.models.pieces.Piece

class HorizontalTakeChecker(private val chessBoard: ChessBoard) : TakeChecker {

  override fun getPossibleTakes(piece: Piece): Set<BoardPosition> {
    return getPossibleHorizontalTakes(piece)
  }

  private fun getPossibleHorizontalTakes(piece: Piece): Set<BoardPosition> {
    val possibleMoves = mutableSetOf<BoardPosition>()
    val rightSideMoves = getRightSidePossibleTakes(piece)
    val leftSideMoves = getLeftSidePossibleTakes(piece)
    possibleMoves.addAll(rightSideMoves)
    possibleMoves.addAll(leftSideMoves)
    return possibleMoves
  }

  private fun getRightSidePossibleTakes(piece: Piece): Set<BoardPosition> {
    val possibleTakes = mutableSetOf<BoardPosition>()
    val rowIndex = piece.getCurrentPosition().rowIndex
    val startColumnIndex = piece.getCurrentPosition().columnIndex + 1
    (startColumnIndex until 8).forEachIndexed { step, columnIndex ->
      if (step >= piece.maxTakeSteps()) {
        return possibleTakes
      }
      val nextBoardPosition = BoardPosition(rowIndex, columnIndex)
      val nextPiece = chessBoard.getPieceAtPosition(nextBoardPosition)
      if (nextPiece == null) {
        //move on
      } else {
        if (nextPiece.isOpposite(piece)) {
          possibleTakes.add(nextBoardPosition)
        }
        return possibleTakes
      }
    }
    return possibleTakes
  }

  private fun getLeftSidePossibleTakes(piece: Piece): Set<BoardPosition> {
    val possibleTakes = mutableSetOf<BoardPosition>()
    val rowIndex = piece.getCurrentPosition().rowIndex
    val startColumnIndex = piece.getCurrentPosition().columnIndex - 1
    (startColumnIndex downTo 0).forEachIndexed { step, columnIndex ->
      if (step >= piece.maxTakeSteps()) {
        return possibleTakes
      }
      val nextBoardPosition = BoardPosition(rowIndex, columnIndex)
      val nextPiece = chessBoard.getPieceAtPosition(nextBoardPosition)
      if (nextPiece == null) {
        //move on
      } else {
        if (nextPiece.isOpposite(piece)) {
          possibleTakes.add(nextBoardPosition)
        }
        return possibleTakes
      }
    }
    return possibleTakes
  }

}
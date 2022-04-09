package com.heshten.core.logic.takecheckers

import com.heshten.core.board.ChessBoard
import com.heshten.core.logic.PositionExclude
import com.heshten.core.logic.TakeChecker
import com.heshten.core.models.BoardPosition
import com.heshten.core.models.pieces.Piece

class HorizontalTakeChecker : TakeChecker {

  override fun getPossibleTakes(piece: Piece, chessBoard: ChessBoard): Set<BoardPosition> {
    return getPossibleHorizontalTakes(piece, chessBoard)
  }

  private fun getPossibleHorizontalTakes(piece: Piece, chessBoard: ChessBoard): Set<BoardPosition> {
    val possibleMoves = mutableSetOf<BoardPosition>()
    val rightSideMoves = getRightSidePossibleTakes(piece, chessBoard)
    val leftSideMoves = getLeftSidePossibleTakes(piece, chessBoard)
    possibleMoves.addAll(rightSideMoves)
    possibleMoves.addAll(leftSideMoves)
    PositionExclude.excludePositionsOutOfBoardInPlace(possibleMoves)
    return possibleMoves
  }

  private fun getRightSidePossibleTakes(piece: Piece, chessBoard: ChessBoard): Set<BoardPosition> {
    val possibleTakes = mutableSetOf<BoardPosition>()
    val rowIndex = piece.boardPosition.rowIndex
    val startColumnIndex = piece.boardPosition.columnIndex + 1
    (startColumnIndex until 8).forEachIndexed { step, columnIndex ->
      if (step >= piece.maxTakeSteps()) {
        return possibleTakes
      }
      val nextBoardPosition = BoardPosition(rowIndex, columnIndex)
      val nextPiece = chessBoard.getPieceAtPosition(nextBoardPosition)
      if (nextPiece == null) {
        //move on
      } else {
        if (piece.direction != nextPiece.direction) {
          possibleTakes.add(nextBoardPosition)
        }
        return possibleTakes
      }
    }
    return possibleTakes
  }

  private fun getLeftSidePossibleTakes(piece: Piece, chessBoard: ChessBoard): Set<BoardPosition> {
    val possibleTakes = mutableSetOf<BoardPosition>()
    val rowIndex = piece.boardPosition.rowIndex
    val startColumnIndex = piece.boardPosition.columnIndex - 1
    (startColumnIndex downTo 0).forEachIndexed { step, columnIndex ->
      if (step >= piece.maxTakeSteps()) {
        return possibleTakes
      }
      val nextBoardPosition = BoardPosition(rowIndex, columnIndex)
      val nextPiece = chessBoard.getPieceAtPosition(nextBoardPosition)
      if (nextPiece == null) {
        //move on
      } else {
        if (piece.direction != nextPiece.direction) {
          possibleTakes.add(nextBoardPosition)
        }
        return possibleTakes
      }
    }
    return possibleTakes
  }
}

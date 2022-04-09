package com.heshten.core.logic.movecheckers

import com.heshten.core.board.ChessBoard
import com.heshten.core.logic.MoveChecker
import com.heshten.core.logic.PositionExclude
import com.heshten.core.models.BoardPosition
import com.heshten.core.models.pieces.Piece

class DiagonalMovesChecker : MoveChecker {

  override fun getPossibleMoves(piece: Piece, chessBoard: ChessBoard): Set<BoardPosition> {
    return getPossibleDiagonalMoves(piece, chessBoard)
  }

  private fun getPossibleDiagonalMoves(piece: Piece, chessBoard: ChessBoard): Set<BoardPosition> {
    val possibleMoves = mutableSetOf<BoardPosition>()
    val leftUpPossibleMoves = getLeftUpPossibleMoves(piece, chessBoard)
    val rightUpPossibleMoves = getRightUpPossibleMoves(piece, chessBoard)
    val leftDownPossibleMoves = getLeftDownPossibleMoves(piece, chessBoard)
    val rightDownPossibleMoves = getRightDownPossibleMoves(piece, chessBoard)
    possibleMoves.addAll(leftUpPossibleMoves)
    possibleMoves.addAll(rightUpPossibleMoves)
    possibleMoves.addAll(leftDownPossibleMoves)
    possibleMoves.addAll(rightDownPossibleMoves)
    PositionExclude.excludePositionsOutOfBoardInPlace(possibleMoves)
    return possibleMoves
  }

  private fun getLeftUpPossibleMoves(piece: Piece, chessBoard: ChessBoard): Set<BoardPosition> {
    val possibleMoves = mutableSetOf<BoardPosition>()
    val startRowIndex = piece.boardPosition.rowIndex - 1
    val startColumnIndex = piece.boardPosition.columnIndex - 1
    (0 until 8).forEachIndexed { step, shift ->
      if (step >= piece.maxSteps()) {
        return possibleMoves
      }
      val nextValidLeftUpPosition =
        BoardPosition(startRowIndex - shift, startColumnIndex - shift)
      if (!chessBoard.hasPieceAtPosition(nextValidLeftUpPosition)) {
        possibleMoves.add(nextValidLeftUpPosition)
      } else {
        return possibleMoves
      }
    }
    return possibleMoves
  }

  private fun getRightUpPossibleMoves(piece: Piece, chessBoard: ChessBoard): Set<BoardPosition> {
    val possibleMoves = mutableSetOf<BoardPosition>()
    val startRowIndex = piece.boardPosition.rowIndex - 1
    val startColumnIndex = piece.boardPosition.columnIndex + 1
    (0 until 8).forEachIndexed { step, shift ->
      if (step >= piece.maxSteps()) {
        return possibleMoves
      }
      val nextValidRightUpPosition =
        BoardPosition(startRowIndex - shift, startColumnIndex + shift)
      if (!chessBoard.hasPieceAtPosition(nextValidRightUpPosition)) {
        possibleMoves.add(nextValidRightUpPosition)
      } else {
        return possibleMoves
      }
    }
    return possibleMoves
  }

  private fun getLeftDownPossibleMoves(piece: Piece, chessBoard: ChessBoard): Set<BoardPosition> {
    val possibleMoves = mutableSetOf<BoardPosition>()
    val startRowIndex = piece.boardPosition.rowIndex + 1
    val startColumnIndex = piece.boardPosition.columnIndex - 1
    (0 until 8).forEachIndexed { step, shift ->
      if (step >= piece.maxSteps()) {
        return possibleMoves
      }
      val nextValidLeftDownPosition =
        BoardPosition(startRowIndex + shift, startColumnIndex - shift)
      if (!chessBoard.hasPieceAtPosition(nextValidLeftDownPosition)) {
        possibleMoves.add(nextValidLeftDownPosition)
      } else {
        return possibleMoves
      }
    }
    return possibleMoves
  }

  private fun getRightDownPossibleMoves(piece: Piece, chessBoard: ChessBoard): Set<BoardPosition> {
    val possibleMoves = mutableSetOf<BoardPosition>()
    val startRowIndex = piece.boardPosition.rowIndex + 1
    val startColumnIndex = piece.boardPosition.columnIndex + 1
    (0 until 8).forEachIndexed { step, shift ->
      if (step >= piece.maxSteps()) {
        return possibleMoves
      }
      val nextValidRightDownPosition =
        BoardPosition(startRowIndex + shift, startColumnIndex + shift)
      if (!chessBoard.hasPieceAtPosition(nextValidRightDownPosition)) {
        possibleMoves.add(nextValidRightDownPosition)
      } else {
        return possibleMoves
      }
    }
    return possibleMoves
  }
}

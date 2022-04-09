package com.heshten.core.logic.takecheckers

import com.heshten.core.board.ChessBoard
import com.heshten.core.logic.PositionExclude
import com.heshten.core.logic.TakeChecker
import com.heshten.core.models.BoardPosition
import com.heshten.core.models.Direction
import com.heshten.core.models.pieces.Piece

class DiagonalTakeChecker : TakeChecker {

  override fun getPossibleTakes(piece: Piece, chessBoard: ChessBoard): Set<BoardPosition> {
    return getPossibleDiagonalTakes(piece, chessBoard)
  }

  private fun getPossibleDiagonalTakes(piece: Piece, chessBoard: ChessBoard): Set<BoardPosition> {
    val possibleTakes = mutableSetOf<BoardPosition>()
    val leftUpPossibleTakes = getLeftUpPossibleMoves(piece, chessBoard)
    val rightUpPossibleTakes = getRightUpPossibleMoves(piece, chessBoard)
    val leftDownPossibleTakes = getLeftDownPossibleMoves(piece, chessBoard)
    val rightDownPossibleTakes = getRightDownPossibleMoves(piece, chessBoard)
    if (piece.canMoveBehind()) {
      possibleTakes.addAll(leftUpPossibleTakes)
      possibleTakes.addAll(rightUpPossibleTakes)
      possibleTakes.addAll(leftDownPossibleTakes)
      possibleTakes.addAll(rightDownPossibleTakes)
    } else {
      //detect which direction is allowed to be calculated for curtain piece
      when (piece.direction) {
        Direction.UP -> {
          possibleTakes.addAll(leftUpPossibleTakes)
          possibleTakes.addAll(rightUpPossibleTakes)
        }
        Direction.DOWN -> {
          possibleTakes.addAll(leftDownPossibleTakes)
          possibleTakes.addAll(rightDownPossibleTakes)
        }
      }
    }
    PositionExclude.excludePositionsOutOfBoardInPlace(possibleTakes)
    return possibleTakes
  }

  private fun getLeftUpPossibleMoves(piece: Piece, chessBoard: ChessBoard): Set<BoardPosition> {
    val possibleTakes = mutableSetOf<BoardPosition>()
    val startRowIndex = piece.boardPosition.rowIndex - 1
    val startColumnIndex = piece.boardPosition.columnIndex - 1
    (0 until 8).forEachIndexed { step, shift ->
      if (step >= piece.maxTakeSteps()) {
        return possibleTakes
      }
      val nextValidLeftUpPosition =
        BoardPosition(startRowIndex - shift, startColumnIndex - shift)
      val nextPiece = chessBoard.getPieceAtPosition(nextValidLeftUpPosition)
      if (nextPiece == null) {
        // move on
      } else {
        if (piece.pieceSide != nextPiece.pieceSide) {
          possibleTakes.add(nextValidLeftUpPosition)
        }
        return possibleTakes
      }
    }
    return possibleTakes
  }

  private fun getRightUpPossibleMoves(piece: Piece, chessBoard: ChessBoard): Set<BoardPosition> {
    val possibleTakes = mutableSetOf<BoardPosition>()
    val startRowIndex = piece.boardPosition.rowIndex - 1
    val startColumnIndex = piece.boardPosition.columnIndex + 1
    (0 until 8).forEachIndexed { step, shift ->
      if (step >= piece.maxTakeSteps()) {
        return possibleTakes
      }
      val nextValidRightUpPosition =
        BoardPosition(startRowIndex - shift, startColumnIndex + shift)
      val nextPiece = chessBoard.getPieceAtPosition(nextValidRightUpPosition)
      if (nextPiece == null) {
        //move on
      } else {
        if (piece.pieceSide != nextPiece.pieceSide) {
          possibleTakes.add(nextValidRightUpPosition)
        }
        return possibleTakes
      }
    }
    return possibleTakes
  }

  private fun getLeftDownPossibleMoves(piece: Piece, chessBoard: ChessBoard): Set<BoardPosition> {
    val possibleTakes = mutableSetOf<BoardPosition>()
    val startRowIndex = piece.boardPosition.rowIndex + 1
    val startColumnIndex = piece.boardPosition.columnIndex - 1
    (0 until 8).forEachIndexed { step, shift ->
      if (step >= piece.maxTakeSteps()) {
        return possibleTakes
      }
      val nextValidLeftDownPosition =
        BoardPosition(startRowIndex + shift, startColumnIndex - shift)
      val nextPiece = chessBoard.getPieceAtPosition(nextValidLeftDownPosition)
      if (nextPiece == null) {
        //move on
      } else {
        if (piece.pieceSide != nextPiece.pieceSide) {
          possibleTakes.add(nextValidLeftDownPosition)
        }
        return possibleTakes
      }
    }
    return possibleTakes
  }

  private fun getRightDownPossibleMoves(piece: Piece, chessBoard: ChessBoard): Set<BoardPosition> {
    val possibleMoves = mutableSetOf<BoardPosition>()
    val startRowIndex = piece.boardPosition.rowIndex + 1
    val startColumnIndex = piece.boardPosition.columnIndex + 1
    (0 until 8).forEachIndexed { step, shift ->
      if (step >= piece.maxTakeSteps()) {
        return possibleMoves
      }
      val nextValidRightDownPosition =
        BoardPosition(startRowIndex + shift, startColumnIndex + shift)
      val nextPiece = chessBoard.getPieceAtPosition(nextValidRightDownPosition)
      if (nextPiece == null) {
        //move on
      } else {
        if (piece.pieceSide != nextPiece.pieceSide) {
          possibleMoves.add(nextValidRightDownPosition)
        }
        return possibleMoves
      }
    }
    return possibleMoves
  }
}

package com.heshten.core.logic.takecheckers

import com.heshten.core.board.ChessBoard
import com.heshten.core.logic.PositionExcluder
import com.heshten.core.logic.TakeChecker
import com.heshten.core.models.BoardPosition
import com.heshten.core.models.Direction
import com.heshten.core.models.opposite
import com.heshten.core.models.pieces.Piece

class DiagonalTakeChecker(private val chessBoard: ChessBoard) : TakeChecker {

  override fun getPossibleTakes(piece: Piece): Set<BoardPosition> {
    return getPossibleDiagonalTakes(piece)
  }

  private fun getPossibleDiagonalTakes(piece: Piece): Set<BoardPosition> {
    val possibleTakes = mutableSetOf<BoardPosition>()
    val leftUpPossibleTakes = getLeftUpPossibleMoves(piece)
    val rightUpPossibleTakes = getRightUpPossibleMoves(piece)
    val leftDownPossibleTakes = getLeftDownPossibleMoves(piece)
    val rightDownPossibleTakes = getRightDownPossibleMoves(piece)
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
    PositionExcluder.excludePositionsOutOfBoardInPlace(possibleTakes)
    return possibleTakes
  }

  private fun getLeftUpPossibleMoves(piece: Piece): Set<BoardPosition> {
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

  private fun getRightUpPossibleMoves(piece: Piece): Set<BoardPosition> {
    val possibleTakes = mutableSetOf<BoardPosition>()
    val startRowIndex = piece.boardPosition.rowIndex - 1
    val startColumnIndex = piece.boardPosition.columnIndex + 1
    (0 until 8).forEachIndexed { step, shift ->
      if (step >= piece.maxTakeSteps()
      ) {
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

  private fun getLeftDownPossibleMoves(piece: Piece): Set<BoardPosition> {
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

  private fun getRightDownPossibleMoves(piece: Piece): Set<BoardPosition> {
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

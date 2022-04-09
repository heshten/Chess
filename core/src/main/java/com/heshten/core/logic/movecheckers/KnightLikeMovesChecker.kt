package com.heshten.core.logic.movecheckers

import com.heshten.core.board.ChessBoard
import com.heshten.core.logic.MoveChecker
import com.heshten.core.logic.PositionExclude
import com.heshten.core.models.BoardPosition
import com.heshten.core.models.pieces.Piece

class KnightLikeMovesChecker : MoveChecker {

  override fun getPossibleMoves(piece: Piece, chessBoard: ChessBoard): Set<BoardPosition> {
    return getKnightLikePossibleMoves(piece, chessBoard)
  }

  private fun getKnightLikePossibleMoves(piece: Piece, chessBoard: ChessBoard): Set<BoardPosition> {
    val possiblePositions = mutableSetOf<BoardPosition>()
    val startRowIndex = piece.boardPosition.rowIndex
    val startColumnIndex = piece.boardPosition.columnIndex
    val upLeftPosition = BoardPosition(startRowIndex - 2, startColumnIndex - 1)
    val upRightPosition = BoardPosition(startRowIndex - 2, startColumnIndex + 1)
    val leftUpPosition = BoardPosition(startRowIndex - 1, startColumnIndex - 2)
    val leftDownPosition = BoardPosition(startRowIndex - 1, startColumnIndex + 2)
    val rightUpPosition = BoardPosition(startRowIndex + 1, startColumnIndex - 2)
    val rightDownPosition = BoardPosition(startRowIndex + 1, startColumnIndex + 2)
    val bottomLeftPosition = BoardPosition(startRowIndex + 2, startColumnIndex - 1)
    val bottomRightPosition = BoardPosition(startRowIndex + 2, startColumnIndex + 1)
    maybeAddPossiblePosition(chessBoard, upLeftPosition, possiblePositions)
    maybeAddPossiblePosition(chessBoard, upRightPosition, possiblePositions)
    maybeAddPossiblePosition(chessBoard, leftUpPosition, possiblePositions)
    maybeAddPossiblePosition(chessBoard, leftDownPosition, possiblePositions)
    maybeAddPossiblePosition(chessBoard, rightUpPosition, possiblePositions)
    maybeAddPossiblePosition(chessBoard, rightDownPosition, possiblePositions)
    maybeAddPossiblePosition(chessBoard, bottomLeftPosition, possiblePositions)
    maybeAddPossiblePosition(chessBoard, bottomRightPosition, possiblePositions)
    PositionExclude.excludePositionsOutOfBoardInPlace(possiblePositions)
    return possiblePositions
  }

  private fun maybeAddPossiblePosition(
    chessBoard: ChessBoard,
    possiblePosition: BoardPosition,
    possibleMovesContainer: MutableSet<BoardPosition>
  ) {
    if (!chessBoard.hasPieceAtPosition(possiblePosition)) {
      possibleMovesContainer.add(possiblePosition)
    }
  }
}

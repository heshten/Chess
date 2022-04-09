package com.heshten.core.logic.movecheckers

import com.heshten.core.board.ChessBoard
import com.heshten.core.logic.MoveChecker
import com.heshten.core.logic.PositionExcluder
import com.heshten.core.models.BoardPosition
import com.heshten.core.models.pieces.Piece

class KnightLikeMovesChecker(private val chessBoard: ChessBoard) : MoveChecker {

  override fun getPossibleMoves(piece: Piece): Set<BoardPosition> {
    return getKnightLikePossibleMoves(piece)
  }

  private fun getKnightLikePossibleMoves(piece: Piece): Set<BoardPosition> {
    val possiblePositions = mutableSetOf<BoardPosition>()
    val startRowIndex = piece.getCurrentPosition().rowIndex
    val startColumnIndex = piece.getCurrentPosition().columnIndex
    val upLeftPosition = BoardPosition(startRowIndex - 2, startColumnIndex - 1)
    val upRightPosition = BoardPosition(startRowIndex - 2, startColumnIndex + 1)
    val leftUpPosition = BoardPosition(startRowIndex - 1, startColumnIndex - 2)
    val leftDownPosition = BoardPosition(startRowIndex - 1, startColumnIndex + 2)
    val rightUpPosition = BoardPosition(startRowIndex + 1, startColumnIndex - 2)
    val rightDownPosition = BoardPosition(startRowIndex + 1, startColumnIndex + 2)
    val bottomLeftPosition = BoardPosition(startRowIndex + 2, startColumnIndex - 1)
    val bottomRightPosition = BoardPosition(startRowIndex + 2, startColumnIndex + 1)
    maybeAddPossiblePosition(upLeftPosition, possiblePositions)
    maybeAddPossiblePosition(upRightPosition, possiblePositions)
    maybeAddPossiblePosition(leftUpPosition, possiblePositions)
    maybeAddPossiblePosition(leftDownPosition, possiblePositions)
    maybeAddPossiblePosition(rightUpPosition, possiblePositions)
    maybeAddPossiblePosition(rightDownPosition, possiblePositions)
    maybeAddPossiblePosition(bottomLeftPosition, possiblePositions)
    maybeAddPossiblePosition(bottomRightPosition, possiblePositions)
    PositionExcluder.excludePositionsOutOfBoardInPlace(possiblePositions)
    return possiblePositions
  }

  private fun maybeAddPossiblePosition(
    possiblePosition: BoardPosition,
    possibleMovesContainer: MutableSet<BoardPosition>
  ) {
    if (positionIsOnBoard(possiblePosition) && !chessBoard.hasPieceAtPosition(possiblePosition)) {
      possibleMovesContainer.add(possiblePosition)
    }
  }

  private fun positionIsOnBoard(position: BoardPosition): Boolean {
    return position.columnIndex in 0..7 && position.rowIndex in 0..7
  }
}

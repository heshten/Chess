package com.heshten.core.logic.takecheckers

import com.heshten.core.board.ChessBoard
import com.heshten.core.logic.PositionExcluder
import com.heshten.core.logic.TakeChecker
import com.heshten.core.models.BoardPosition
import com.heshten.core.models.pieces.Piece

class KnightLikeTakeChecker(private val chessBoard: ChessBoard) : TakeChecker {

  override fun getPossibleTakes(piece: Piece): Set<BoardPosition> {
    return getPossibleKnightLikeTakes(piece)
  }

  private fun getPossibleKnightLikeTakes(piece: Piece): Set<BoardPosition> {
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
    maybeAddPossiblePosition(piece, upLeftPosition, possiblePositions)
    maybeAddPossiblePosition(piece, upRightPosition, possiblePositions)
    maybeAddPossiblePosition(piece, leftUpPosition, possiblePositions)
    maybeAddPossiblePosition(piece, leftDownPosition, possiblePositions)
    maybeAddPossiblePosition(piece, rightUpPosition, possiblePositions)
    maybeAddPossiblePosition(piece, rightDownPosition, possiblePositions)
    maybeAddPossiblePosition(piece, bottomLeftPosition, possiblePositions)
    maybeAddPossiblePosition(piece, bottomRightPosition, possiblePositions)
    PositionExcluder.excludePositionsOutOfBoardInPlace(possiblePositions)
    return possiblePositions
  }

  private fun maybeAddPossiblePosition(
    piece: Piece,
    possiblePosition: BoardPosition,
    possibleTakesContainer: MutableSet<BoardPosition>
  ) {
    val nextPiece = chessBoard.getPieceAtPosition(possiblePosition)
    if (nextPiece != null && piece.direction != nextPiece.direction) {
      possibleTakesContainer.add(possiblePosition)
    }
  }
}

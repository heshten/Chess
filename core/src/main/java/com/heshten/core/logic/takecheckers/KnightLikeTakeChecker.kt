package com.heshten.core.logic.takecheckers

import com.heshten.core.board.ChessBoard
import com.heshten.core.logic.PositionExclude
import com.heshten.core.logic.TakeChecker
import com.heshten.core.models.BoardPosition
import com.heshten.core.models.pieces.Piece

class KnightLikeTakeChecker : TakeChecker {

  override fun getPossibleTakes(piece: Piece, chessBoard: ChessBoard): Set<BoardPosition> {
    return getPossibleKnightLikeTakes(piece, chessBoard)
  }

  private fun getPossibleKnightLikeTakes(piece: Piece, chessBoard: ChessBoard): Set<BoardPosition> {
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
    maybeAddPossiblePosition(piece, chessBoard, upLeftPosition, possiblePositions)
    maybeAddPossiblePosition(piece, chessBoard, upRightPosition, possiblePositions)
    maybeAddPossiblePosition(piece, chessBoard, leftUpPosition, possiblePositions)
    maybeAddPossiblePosition(piece, chessBoard, leftDownPosition, possiblePositions)
    maybeAddPossiblePosition(piece, chessBoard, rightUpPosition, possiblePositions)
    maybeAddPossiblePosition(piece, chessBoard, rightDownPosition, possiblePositions)
    maybeAddPossiblePosition(piece, chessBoard, bottomLeftPosition, possiblePositions)
    maybeAddPossiblePosition(piece, chessBoard, bottomRightPosition, possiblePositions)
    PositionExclude.excludePositionsOutOfBoardInPlace(possiblePositions)
    return possiblePositions
  }

  private fun maybeAddPossiblePosition(
    piece: Piece,
    chessBoard: ChessBoard,
    possiblePosition: BoardPosition,
    possibleTakesContainer: MutableSet<BoardPosition>
  ) {
    val nextPiece = chessBoard.getPieceAtPosition(possiblePosition)
    if (nextPiece != null && piece.direction != nextPiece.direction) {
      possibleTakesContainer.add(possiblePosition)
    }
  }
}

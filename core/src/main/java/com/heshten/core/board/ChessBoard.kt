package com.heshten.core.board

import com.heshten.core.models.BoardPosition
import com.heshten.core.models.Direction
import com.heshten.core.models.pieces.King
import com.heshten.core.models.pieces.Pawn
import com.heshten.core.models.pieces.Piece
import com.heshten.core.models.pieces.Queen

/**
 * A board holder class that is the source of truth for the playing board.
 */
class ChessBoard(pieces: Set<Piece>) {

  private val possibleMovesPositions = mutableSetOf<BoardPosition>()
  private val pieces: MutableSet<Piece> = mutableSetOf()
  private var selectedPiece: Piece? = null

  init {
    this.pieces.addAll(pieces)
  }

  fun getAllPieces(): Set<Piece> {
    return pieces
  }

  fun getPossibleMovesPositions(): Set<BoardPosition> {
    return possibleMovesPositions
  }

  fun isPossibleMoveTo(position: BoardPosition): Boolean {
    return possibleMovesPositions.contains(position)
  }

  fun hasPieceAtPosition(boardPosition: BoardPosition): Boolean {
    return getPieceAtPosition(boardPosition) != null
  }

  fun moveSelectedPieceToPosition(boardPosition: BoardPosition) {
    val selectedPieceLocal = selectedPiece ?: return
    // check if castling
    if (selectedPieceLocal is King) {
      if (
        selectedPieceLocal.firstMovePerformed.not() &&
        selectedPieceLocal.direction == Direction.UP &&
        boardPosition.rowIndex == 7 &&
        boardPosition.columnIndex == 6
      ) {
        val castlingRook = getPieceAtPosition(BoardPosition(7, 7))!!
        val updatedRook = updatePiece(castlingRook, BoardPosition(7, 5))
        pieces.remove(castlingRook)
        pieces.add(updatedRook)
      }
      if (
        selectedPieceLocal.firstMovePerformed.not() &&
        selectedPieceLocal.direction == Direction.DOWN &&
        boardPosition.rowIndex == 0 &&
        boardPosition.columnIndex == 6
      ) {
        val castlingRook = getPieceAtPosition(BoardPosition(0, 7))!!
        val updatedRook = updatePiece(castlingRook, BoardPosition(0, 5))
        pieces.remove(castlingRook)
        pieces.add(updatedRook)
      }
      if (
        selectedPieceLocal.firstMovePerformed.not() &&
        selectedPieceLocal.direction == Direction.UP &&
        boardPosition.rowIndex == 7 &&
        boardPosition.columnIndex == 2
      ) {
        val castlingRook = getPieceAtPosition(BoardPosition(7, 0))!!
        val updatedRook = updatePiece(castlingRook, BoardPosition(7, 3))
        pieces.remove(castlingRook)
        pieces.add(updatedRook)
      }
      if (
        selectedPieceLocal.firstMovePerformed.not() &&
        selectedPieceLocal.direction == Direction.DOWN &&
        boardPosition.rowIndex == 0 &&
        boardPosition.columnIndex == 2
      ) {
        val castlingRook = getPieceAtPosition(BoardPosition(0, 0))!!
        val updatedRook = updatePiece(castlingRook, BoardPosition(0, 3))
        pieces.remove(castlingRook)
        pieces.add(updatedRook)
      }
    }
    if (hasPieceAtPosition(boardPosition)) {
      pieces.remove(getPieceAtPosition(boardPosition))
    }
    val updatedPiece = updatePiece(selectedPieceLocal, boardPosition)
    pieces.remove(selectedPieceLocal)
    pieces.add(updatedPiece)
    selectedPiece = null
  }

  fun selectPiece(piece: Piece) {
    selectedPiece = piece
  }

  fun setPossibleMoves(positions: Set<BoardPosition>) {
    clearPossibleMovesPositions()
    possibleMovesPositions.addAll(positions)
  }

  fun clearPossibleMovesPositions() {
    possibleMovesPositions.clear()
  }

  fun getPieceAtPosition(boardPosition: BoardPosition): Piece? {
    return pieces.find { it.boardPosition == boardPosition }
  }

  private fun updatePiece(piece: Piece, boardPosition: BoardPosition): Piece {
    if (piece is Pawn) {
      // check if reached the end of the board
      if ((piece.direction == Direction.DOWN && boardPosition.rowIndex == 7)
        || (piece.direction == Direction.UP && boardPosition.rowIndex == 0)
      ) {
        return Queen(piece.pieceSide, piece.direction, boardPosition, piece.firstMovePerformed)
      }
    }
    return piece.copy(boardPosition = boardPosition, firstMovePerformed = true)
  }
}

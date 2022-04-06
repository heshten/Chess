package com.heshten.chess.core.board

import com.heshten.chess.core.models.BoardPosition
import com.heshten.chess.core.models.pieces.Piece

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

  fun isPossibleMoveTo(position: BoardPosition): Boolean {
    return possibleMovesPositions.contains(position)
  }

  fun hasPieceAtPosition(boardPosition: BoardPosition): Boolean {
    return getPieceForPosition(boardPosition) != null
  }

  fun getPieceForPosition(boardPosition: BoardPosition): Piece? {
    return pieces.find { it.getCurrentPosition() == boardPosition }
  }

  fun moveSelectedPieceToPosition(boardPosition: BoardPosition) {
    selectedPiece?.moveTo(boardPosition)
    selectedPiece = null
  }

  fun removePieceAtPosition(boardPosition: BoardPosition) {
    pieces.remove(getPieceForPosition(boardPosition))
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
}

package com.heshten.core.board

import com.heshten.core.models.BoardPosition
import com.heshten.core.models.pieces.Piece

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
    pieces.remove(selectedPieceLocal)
    pieces.add(
      selectedPieceLocal.copy(
        boardPosition = boardPosition,
        firstMovePerformed = true
      )
    )
    selectedPiece = null
  }

  fun removePieceAtPosition(boardPosition: BoardPosition) {
    pieces.remove(getPieceAtPosition(boardPosition))
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
}

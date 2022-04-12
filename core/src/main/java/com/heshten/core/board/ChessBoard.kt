package com.heshten.core.board

import com.heshten.core.models.BoardPosition
import com.heshten.core.models.Direction
import com.heshten.core.models.Move
import com.heshten.core.models.PieceSide
import com.heshten.core.models.pieces.King
import com.heshten.core.models.pieces.Pawn
import com.heshten.core.models.pieces.Piece
import com.heshten.core.models.pieces.Queen

/**
 * A board holder class that is the source of truth for the playing board.
 */
class ChessBoard(pieces: Set<Piece>) {

  private val pieces: MutableSet<Piece> = mutableSetOf()

  init {
    this.pieces.addAll(pieces)
  }

  /**
   * Returns all the pieces from the board.
   * */
  fun getAllPieces(): Set<Piece> {
    return pieces
  }

  /**
   * Returns all the pieces from the board with specific piece side.
   *
   * @param pieceSide - pieces with this side will be returned.
   * */
  fun getAllPiecesForSide(pieceSide: PieceSide): Set<Piece> {
    return getAllPieces().filter { it.pieceSide == pieceSide }.toSet()
  }

  /**
   * Checks if the chessboard has any piece at [BoardPosition].
   *
   * Warning: relays on the [getPieceAtPosition] method and depends on its runtime complexity.
   * */
  fun hasPieceAtPosition(boardPosition: BoardPosition): Boolean {
    return getPieceAtPosition(boardPosition) != null
  }

  /**
   * Performs the specific [Move] on the given board.
   * */
  private fun doMoveInternal(move: Move) {
    // check if castling
    if (move.piece is King) {
      if (
        move.piece.firstMovePerformed.not() &&
        move.piece.direction == Direction.UP &&
        move.toPosition.rowIndex == 7 &&
        move.toPosition.columnIndex == 6
      ) {
        val castlingRook = getPieceAtPosition(BoardPosition(7, 7))!!
        val updatedRook = updatePiecePosition(castlingRook, BoardPosition(7, 5))
        pieces.remove(castlingRook)
        pieces.add(updatedRook)
      }
      if (
        move.piece.firstMovePerformed.not() &&
        move.piece.direction == Direction.DOWN &&
        move.toPosition.rowIndex == 0 &&
        move.toPosition.columnIndex == 6
      ) {
        val castlingRook = getPieceAtPosition(BoardPosition(0, 7))!!
        val updatedRook = updatePiecePosition(castlingRook, BoardPosition(0, 5))
        pieces.remove(castlingRook)
        pieces.add(updatedRook)
      }
      if (
        move.piece.firstMovePerformed.not() &&
        move.piece.direction == Direction.UP &&
        move.toPosition.rowIndex == 7 &&
        move.toPosition.columnIndex == 2
      ) {
        val castlingRook = getPieceAtPosition(BoardPosition(7, 0))!!
        val updatedRook = updatePiecePosition(castlingRook, BoardPosition(7, 3))
        pieces.remove(castlingRook)
        pieces.add(updatedRook)
      }
      if (
        move.piece.firstMovePerformed.not() &&
        move.piece.direction == Direction.DOWN &&
        move.toPosition.rowIndex == 0 &&
        move.toPosition.columnIndex == 2
      ) {
        val castlingRook = getPieceAtPosition(BoardPosition(0, 0))!!
        val updatedRook = updatePiecePosition(castlingRook, BoardPosition(0, 3))
        pieces.remove(castlingRook)
        pieces.add(updatedRook)
      }
    }
    if (hasPieceAtPosition(move.toPosition)) {
      pieces.remove(getPieceAtPosition(move.toPosition))
    }
    val updatedPiece = updatePiecePosition(move.piece, move.toPosition)
    pieces.remove(move.piece)
    pieces.add(updatedPiece)
  }

  fun doMove(move: Move) {
    doMoveInternal(move)
  }

  fun undo() {
    TODO()
  }

  /**
   * Return piece at a specific position if any.
   *
   * Warning: this method iterates through the whole collection and the runtime complexity is O(N).
   * */
  fun getPieceAtPosition(boardPosition: BoardPosition): Piece? {
    return pieces.find { it.boardPosition == boardPosition }
  }

  /**
   * Updates the piece position through recreating the piece with the given position.
   *
   * Changes the [Piece.firstMovePerformed] flag to true after each invocation.
   *
   * Checks if the given [Piece] is [Pawn] that reaches the end of the board
   * so that it could be transformed to the [Queen].
   *
   * @return updated [Piece] after performing given movement.
   * */
  private fun updatePiecePosition(piece: Piece, boardPosition: BoardPosition): Piece {
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

package com.heshten.core.board

import com.heshten.core.models.BoardPosition
import com.heshten.core.models.Direction
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
  private val possibleMovesPositions = mutableSetOf<BoardPosition>()

  private var selectedPiece: Piece? = null

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
   * Returns possible movement positions.
   * */
  fun getPossibleMovesPositions(): Set<BoardPosition> {
    return possibleMovesPositions
  }

  /**
   * Sets possible movement positions.
   * */
  fun setPossibleMoves(positions: Set<BoardPosition>) {
    possibleMovesPositions.clear()
    possibleMovesPositions.addAll(positions)
  }

  /**
   * Returns weather or not [possibleMovesPositions] contains given [BoardPosition].
   * */
  fun isPossibleMoveTo(position: BoardPosition): Boolean {
    return possibleMovesPositions.contains(position)
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
   * Moves [selectedPiece] object to the specific [BoardPosition].
   *
   * Checks if the [selectedPiece] is [King] that is castling.
   * */
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
        val updatedRook = updatePiecePosition(castlingRook, BoardPosition(7, 5))
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
        val updatedRook = updatePiecePosition(castlingRook, BoardPosition(0, 5))
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
        val updatedRook = updatePiecePosition(castlingRook, BoardPosition(7, 3))
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
        val updatedRook = updatePiecePosition(castlingRook, BoardPosition(0, 3))
        pieces.remove(castlingRook)
        pieces.add(updatedRook)
      }
    }
    if (hasPieceAtPosition(boardPosition)) {
      pieces.remove(getPieceAtPosition(boardPosition))
    }
    val updatedPiece = updatePiecePosition(selectedPieceLocal, boardPosition)
    pieces.remove(selectedPieceLocal)
    pieces.add(updatedPiece)
    selectedPiece = null
  }

  /**
   * Sets the selected piece on the board.
   *
   * Could be used to perform [moveSelectedPieceToPosition] after.
   * */
  fun selectPiece(piece: Piece) {
    selectedPiece = piece
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

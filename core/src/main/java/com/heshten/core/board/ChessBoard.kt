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
class ChessBoard(
  pieces: Set<Piece>,
  private val possibleMovesCalculator: PossibleMovesCalculator
) {
  private val pieces = mutableSetOf<Piece>()

  // ugly and not a performant way of storing game history.
  private val piecesSnapshots = mutableListOf<Set<Piece>>()

  init {
    this.pieces.addAll(pieces)
    this.piecesSnapshots.add(pieces)
  }

  fun copy(): ChessBoard {
    return ChessBoard(getAllPieces(), possibleMovesCalculator)
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
   * Returns all the possible moves for given [Piece].
   * */
  fun getPossibleMovesForPiece(piece: Piece): Set<Move> {
    return possibleMovesCalculator.calculatePossibleMovesForPiece(piece, this)
  }

  /**
   * Performs the specific [Move] on the given board.
   * */
  private fun doMoveInternal(move: Move) {
    when (move) {
      is Move.Castling -> {
        updateMapWithMove(move.kingMove)
        updateMapWithMove(move.rookMove)
      }
      is Move.Regular -> {
        if (hasPieceAtPosition(move.toPosition)) {
          pieces.remove(getPieceAtPosition(move.toPosition))
        }
        updateMapWithMove(move)
      }
    }
  }

  private fun updateMapWithMove(move: Move.Regular) {
    val pieceBeforeMove = move.piece
    val pieceAfterMove = updatePiece(move)
    pieces.remove(pieceBeforeMove)
    pieces.add(pieceAfterMove)
  }

  fun doMove(move: Move) {
    piecesSnapshots.add(pieces.toSet())
    doMoveInternal(move)
  }

  fun undo() {
    if (piecesSnapshots.size == 1) {
      return
    }
    pieces.clear()
    pieces.addAll(piecesSnapshots.removeLast())
  }

  /**
   * Return piece at a specific position if any.
   *
   * Warning: this method iterates through the whole collection and the runtime complexity is O(N).
   * */
  fun getPieceAtPosition(boardPosition: BoardPosition): Piece? {
    return getAllPieces().find { it.boardPosition == boardPosition }
  }

  /**
   * Returns weather given [PieceSide] is under check.
   * */
  fun isCheck(side: PieceSide): Boolean {
    val king = getAllPiecesForSide(side).first { it is King }
    return possibleMovesCalculator.isUnderAttack(king, this)
  }

  /**
   * Returns weather given [PieceSide] has any move.
   * */
  fun hasNextMoves(side: PieceSide): Boolean {
    var possibleMoves = 0
    getAllPiecesForSide(side).forEach { piece ->
      possibleMoves += getPossibleMovesForPiece(piece).size
      if (possibleMoves > 0) return true
    }
    return false
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
  private fun updatePiece(move: Move.Regular): Piece {
    val piece = move.piece
    val toPosition = move.toPosition
    if (piece is Pawn) {
      // check if reached the end of the board
      if (
        (piece.direction == Direction.DOWN && toPosition.rowIndex == 7) ||
        (piece.direction == Direction.UP && toPosition.rowIndex == 0)
      ) {
        return Queen(piece.pieceSide, piece.direction, toPosition, piece.firstMovePerformed)
      }
    }
    return piece.copy(boardPosition = toPosition, firstMovePerformed = true)
  }
}

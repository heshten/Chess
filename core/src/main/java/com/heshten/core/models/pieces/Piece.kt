package com.heshten.core.models.pieces

import com.heshten.core.models.BoardPosition
import com.heshten.core.models.Direction
import com.heshten.core.models.PieceSide
import com.heshten.core.models.markers.MoveMarker
import com.heshten.core.models.markers.StepMarker
import com.heshten.core.models.markers.TakeMarker

abstract class Piece(
  val pieceSide: PieceSide,
  val direction: Direction,
  val boardPosition: BoardPosition,
  val firstMovePerformed: Boolean
) : MoveMarker, TakeMarker, StepMarker {

  fun copy(
    pieceSide: PieceSide = this.pieceSide,
    direction: Direction = this.direction,
    boardPosition: BoardPosition = this.boardPosition,
    firstMovePerformed: Boolean = this.firstMovePerformed
  ): Piece = when (this) {
    is Bishop -> Bishop(pieceSide, direction, boardPosition, firstMovePerformed)
    is King -> King(pieceSide, direction, boardPosition, firstMovePerformed)
    is Knight -> Knight(pieceSide, direction, boardPosition, firstMovePerformed)
    is Pawn -> Pawn(pieceSide, direction, boardPosition, firstMovePerformed)
    is Queen -> Queen(pieceSide, direction, boardPosition, firstMovePerformed)
    is Rook -> Rook(pieceSide, direction, boardPosition, firstMovePerformed)
    else -> throw IllegalStateException()
  }

  override fun equals(other: Any?): Boolean {
    if (this === other) return true
    if (other !is Piece) return false

    if (pieceSide != other.pieceSide) return false
    if (direction != other.direction) return false
    if (boardPosition != other.boardPosition) return false
    if (firstMovePerformed != other.firstMovePerformed) return false

    return true
  }

  override fun hashCode(): Int {
    var result = pieceSide.hashCode()
    result = 31 * result + direction.hashCode()
    result = 31 * result + boardPosition.hashCode()
    result = 31 * result + firstMovePerformed.hashCode()
    return result
  }
}

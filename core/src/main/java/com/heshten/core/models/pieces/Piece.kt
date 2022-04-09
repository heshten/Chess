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
  private var boardPosition: BoardPosition
) : MoveMarker, TakeMarker, StepMarker {

  private var firstMovePerformed = false

  fun getCurrentPosition(): BoardPosition {
    return boardPosition
  }

  fun isOpposite(otherPiece: Piece): Boolean {
    return pieceSide != otherPiece.pieceSide
  }

  fun firstMovePerformed(): Boolean {
    return firstMovePerformed
  }

  fun copy(): Piece = when(this) {
    is Bishop -> Bishop(pieceSide, direction, getCurrentPosition())
    is King -> Bishop(pieceSide, direction, getCurrentPosition())
    is Knight -> Bishop(pieceSide, direction, getCurrentPosition())
    is Pawn -> Bishop(pieceSide, direction, getCurrentPosition())
    is Queen -> Bishop(pieceSide, direction, getCurrentPosition())
    is Rook -> Bishop(pieceSide, direction, getCurrentPosition())
    else -> throw IllegalStateException()
  }

  fun moveTo(position: BoardPosition) {
    firstMovePerformed = true
    boardPosition = position
  }
}

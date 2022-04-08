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

  fun getCurrentPosition(): BoardPosition {
    return boardPosition
  }

  fun isOpposite(otherPiece: Piece): Boolean {
    return pieceSide != otherPiece.pieceSide
  }

  open fun moveTo(position: BoardPosition) {
    boardPosition = position
  }
}

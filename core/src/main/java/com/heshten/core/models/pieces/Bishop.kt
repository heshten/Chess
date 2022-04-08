package com.heshten.core.models.pieces

import com.heshten.core.models.BoardPosition
import com.heshten.core.models.Direction
import com.heshten.core.models.PieceSide

class Bishop(
  side: PieceSide,
  direction: Direction,
  position: BoardPosition
) : Piece(side, direction, position) {

  override fun canMoveVertically(): Boolean = false

  override fun canMoveHorizontally(): Boolean = false

  override fun canMoveDiagonally(): Boolean = true

  override fun canMoveKnightLike(): Boolean = false

  override fun canMoveBehind(): Boolean = true

  override fun canTakeVertically(): Boolean = false

  override fun canTakeHorizontally(): Boolean = false

  override fun canTakeDiagonally(): Boolean = true

  override fun canTakeKnightLike(): Boolean = false

  override fun canTakeBehind(): Boolean = true

  override fun maxSteps(): Int = 8

  override fun maxTakeSteps(): Int = 8

}
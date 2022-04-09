package com.heshten.core.models.pieces

import com.heshten.core.models.BoardPosition
import com.heshten.core.models.Direction
import com.heshten.core.models.PieceSide

class Queen(
  side: PieceSide,
  direction: Direction,
  position: BoardPosition,
  firstMovePerformed: Boolean
) : Piece(side, direction, position, firstMovePerformed) {

  override fun canMoveVertically(): Boolean = true

  override fun canMoveHorizontally(): Boolean = true

  override fun canMoveDiagonally(): Boolean = true

  override fun canMoveKnightLike(): Boolean = false

  override fun canMoveBehind(): Boolean = true

  override fun canTakeVertically(): Boolean = true

  override fun canTakeHorizontally(): Boolean = true

  override fun canTakeDiagonally(): Boolean = true

  override fun canTakeKnightLike(): Boolean = false

  override fun canTakeBehind(): Boolean = true

  override fun maxSteps(): Int = 8

  override fun maxTakeSteps(): Int = 8
}

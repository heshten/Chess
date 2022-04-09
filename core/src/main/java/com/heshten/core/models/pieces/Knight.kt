package com.heshten.core.models.pieces

import com.heshten.core.models.BoardPosition
import com.heshten.core.models.Direction
import com.heshten.core.models.PieceSide

class Knight(
  side: PieceSide,
  direction: Direction,
  position: BoardPosition,
  firstMovePerformed: Boolean
) : Piece(side, direction, position, firstMovePerformed) {

  override fun canMoveVertically(): Boolean = false

  override fun canMoveHorizontally(): Boolean = false

  override fun canMoveDiagonally(): Boolean = false

  override fun canMoveKnightLike(): Boolean = true

  override fun canMoveBehind(): Boolean = true

  override fun canTakeVertically(): Boolean = false

  override fun canTakeHorizontally(): Boolean = false

  override fun canTakeDiagonally(): Boolean = false

  override fun canTakeKnightLike(): Boolean = true

  override fun canTakeBehind(): Boolean = true

  override fun maxSteps(): Int = 1

  override fun maxTakeSteps(): Int = 3

}
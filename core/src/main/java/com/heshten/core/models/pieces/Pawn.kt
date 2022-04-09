package com.heshten.core.models.pieces

import com.heshten.core.models.BoardPosition
import com.heshten.core.models.Direction
import com.heshten.core.models.PieceSide

class Pawn(
  side: PieceSide,
  direction: Direction,
  position: BoardPosition
) : Piece(side, direction, position) {

  override fun canMoveVertically(): Boolean = true

  override fun canMoveHorizontally(): Boolean = false

  override fun canMoveDiagonally(): Boolean = false

  override fun canMoveKnightLike(): Boolean = false

  override fun canMoveBehind(): Boolean = false

  override fun canTakeVertically(): Boolean = false

  override fun canTakeHorizontally(): Boolean = false

  override fun canTakeDiagonally(): Boolean = true

  override fun canTakeKnightLike(): Boolean = false

  override fun canTakeBehind(): Boolean = false

  override fun maxSteps(): Int = when (firstMovePerformed()) {
    true -> 1
    false -> 2
  }

  override fun maxTakeSteps(): Int = 1
}

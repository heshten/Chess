package com.heshten.chess.core.models.pieces

import android.graphics.Bitmap
import com.heshten.chess.core.models.BoardPosition
import com.heshten.chess.core.models.Direction
import com.heshten.chess.core.models.PieceSide

class Pawn(
  bitmap: Bitmap,
  side: PieceSide,
  direction: Direction,
  position: BoardPosition
) : Piece(bitmap, side, direction, position) {

  private var firstStepPerformed = false

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

  override fun maxSteps(): Int = when (firstStepPerformed) {
    true -> 1
    false -> 2
  }

  override fun maxTakeSteps(): Int = 1

  override fun moveTo(position: BoardPosition) {
    super.moveTo(position)
    firstStepPerformed = true
  }

}
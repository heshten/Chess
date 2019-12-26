package com.heshten.chess.core.models.pieces

import android.graphics.Bitmap
import com.heshten.chess.core.models.BoardPosition
import com.heshten.chess.core.models.Direction
import com.heshten.chess.core.models.PieceSide

class King(
    bitmap: Bitmap,
    side: PieceSide,
    direction: Direction,
    position: BoardPosition
) : Piece(bitmap, side, direction, position) {

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

    override fun maxSteps(): Int = 1

}
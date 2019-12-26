package com.heshten.chess.core.models.pieces

import android.graphics.Bitmap
import com.heshten.chess.core.models.BoardPosition
import com.heshten.chess.core.models.Direction
import com.heshten.chess.core.models.PieceSide

class Knight(
    bitmap: Bitmap,
    side: PieceSide,
    direction: Direction,
    position: BoardPosition
) : Piece(bitmap, side, direction, position) {

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

}
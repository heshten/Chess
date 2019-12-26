package com.heshten.chess.core.models.pieces

import android.graphics.Bitmap
import com.heshten.chess.core.models.BoardPosition
import com.heshten.chess.core.models.PieceSide

class Rook(
    bitmap: Bitmap,
    side: PieceSide,
    position: BoardPosition
) : Piece(bitmap, side, position) {

    override fun canMoveVertically(): Boolean = true

    override fun canMoveHorizontally(): Boolean = true

    override fun canMoveDiagonally(): Boolean = false

    override fun canMoveKnightLike(): Boolean = false

    override fun canMoveBehind(): Boolean = true

    override fun canTakeVertically(): Boolean = true

    override fun canTakeHorizontally(): Boolean = true

    override fun canTakeDiagonally(): Boolean = false

    override fun canTakeKnightLike(): Boolean = false

    override fun canTakeBehind(): Boolean = true

    override fun maxSteps(): Int = 8

}
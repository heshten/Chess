package com.heshten.chess.core.models.pieces

import android.graphics.Bitmap
import com.heshten.chess.core.models.BoardPosition
import com.heshten.chess.core.models.helpers.PieceSide

class King(
    bitmap: Bitmap,
    side: PieceSide,
    position: BoardPosition
) : Piece(bitmap, side, position) {

    override fun canMoveVertically(): Boolean = true

    override fun canMoveHorizontally(): Boolean = true

    override fun canMoveDiagonally(): Boolean = true

    override fun maxSteps(): Int = 1

}
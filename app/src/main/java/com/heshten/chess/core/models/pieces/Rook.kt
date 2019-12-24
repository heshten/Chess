package com.heshten.chess.core.models.pieces

import android.graphics.Bitmap
import com.heshten.chess.core.models.BoardPosition
import com.heshten.chess.core.models.helpers.PieceSide

class Rook(
    bitmap: Bitmap,
    side: PieceSide,
    position: BoardPosition
) : Piece(bitmap, side, position) {

    override fun canMoveVertically(): Boolean = true

    override fun canMoveHorizontally(): Boolean = true

    override fun canTakeHorizontally(): Boolean = true

    override fun canTakeVertically(): Boolean = true

}
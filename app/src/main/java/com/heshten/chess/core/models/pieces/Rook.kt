package com.heshten.chess.core.models.pieces

import android.graphics.Bitmap
import com.heshten.chess.core.models.BoardPosition

class Rook(
    bitmap: Bitmap,
    position: BoardPosition
) : Piece(bitmap, position) {

    override fun canMoveVertically(): Boolean = true

    override fun canMoveHorizontally(): Boolean = true

}
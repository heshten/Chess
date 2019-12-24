package com.heshten.chess.core.models.pieces

import android.graphics.Bitmap
import com.heshten.chess.core.models.BoardPosition
import com.heshten.chess.core.models.helpers.PieceSide

class Knight(
    bitmap: Bitmap,
    side: PieceSide,
    position: BoardPosition
) :
    Piece(bitmap, side, position) {

    override fun canMoveKnightLike(): Boolean = true

}
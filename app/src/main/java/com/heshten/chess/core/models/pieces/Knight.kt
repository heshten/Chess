package com.heshten.chess.core.models.pieces

import android.graphics.Bitmap
import com.heshten.chess.core.models.BoardPosition

class Knight(bitmap: Bitmap, position: BoardPosition) : Piece(bitmap, position) {

    override fun canMoveKnightLike(): Boolean = true

}
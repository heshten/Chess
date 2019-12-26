package com.heshten.chess.core.models.pieces

import android.graphics.Bitmap
import androidx.annotation.CallSuper
import com.heshten.chess.core.models.BoardPosition
import com.heshten.chess.core.models.Direction
import com.heshten.chess.core.models.PieceSide
import com.heshten.chess.core.models.markers.MoveMarker
import com.heshten.chess.core.models.markers.StepMarker
import com.heshten.chess.core.models.markers.TakeMarker

abstract class Piece(
    val bitmap: Bitmap,
    val pieceSide: PieceSide,
    val direction: Direction,
    private var boardPosition: BoardPosition
) : MoveMarker, TakeMarker, StepMarker {

    fun getCurrentPosition(): BoardPosition {
        return boardPosition.copy()
    }

    fun isOpposite(otherPiece: Piece): Boolean {
        return pieceSide != otherPiece.pieceSide
    }

    @CallSuper
    open fun moveTo(position: BoardPosition) {
        boardPosition = position
    }

}
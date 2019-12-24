package com.heshten.chess.core.models.pieces

import android.graphics.Bitmap
import com.heshten.chess.core.models.BoardPosition
import com.heshten.chess.core.models.helpers.MoveDirection
import com.heshten.chess.core.models.helpers.PieceSide

class Pawn(
    bitmap: Bitmap,
    position: BoardPosition,
    side: PieceSide,
    private val moveDirection: MoveDirection
) : Piece(bitmap, side, position) {

    private var firstStepPerformed = false

    override fun canMoveVertically(): Boolean = true

    override fun canTakeDiagonally(): Boolean = true

    override fun pieceDirection(): MoveDirection = moveDirection

    override fun maxSteps(): Int = when (firstStepPerformed) {
        true -> 1
        false -> 2
    }

    override fun moveTo(position: BoardPosition) {
        super.moveTo(position)
        firstStepPerformed = true
    }

}
package com.heshten.chess.core.models.pieces

import android.graphics.Bitmap
import com.heshten.chess.core.models.BoardPosition
import com.heshten.chess.core.models.helpers.*

abstract class Piece(
    val bitmap: Bitmap,
    private val pieceSide: PieceSide,
    private var boardPosition: BoardPosition
) : MoveHelper, TakeHelper, DirectionHelper, StepHelper {

    override fun canMoveVertically(): Boolean = false

    override fun canMoveHorizontally(): Boolean = false

    override fun canMoveDiagonally(): Boolean = false

    override fun canMoveKnightLike(): Boolean = false

    override fun canTakeVertically(): Boolean = false

    override fun canTakeHorizontally(): Boolean = false

    override fun canTakeDiagonally(): Boolean = false

    override fun canTakeKnightLike(): Boolean = false

    override fun pieceDirection(): MoveDirection = MoveDirection.BOTH

    override fun maxSteps(): Int = 8

    fun getCurrentPosition(): BoardPosition {
        return boardPosition.copy()
    }

    fun isOpposite(otherPiece: Piece): Boolean {
        return pieceSide != otherPiece.pieceSide
    }

    open fun moveTo(position: BoardPosition) {
        boardPosition = position
    }

}
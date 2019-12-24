package com.heshten.chess.core.models.helpers

interface MoveHelper {
    fun canMoveVertically(): Boolean
    fun canMoveHorizontally(): Boolean
    fun canMoveDiagonally(): Boolean
    fun canMoveKnightLike(): Boolean
}
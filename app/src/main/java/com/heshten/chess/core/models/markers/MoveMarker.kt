package com.heshten.chess.core.models.markers

interface MoveMarker {
    fun canMoveVertically(): Boolean
    fun canMoveHorizontally(): Boolean
    fun canMoveDiagonally(): Boolean
    fun canMoveKnightLike(): Boolean
    fun canMoveBehind(): Boolean
}
package com.heshten.chess.core.models.helpers

interface TakeHelper {
    fun canTakeVertically(): Boolean
    fun canTakeHorizontally(): Boolean
    fun canTakeDiagonally(): Boolean
    fun canTakeKnightLike(): Boolean
}
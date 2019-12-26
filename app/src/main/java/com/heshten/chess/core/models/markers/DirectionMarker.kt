package com.heshten.chess.core.models.markers

import com.heshten.chess.core.models.MoveDirection

interface DirectionMarker {

    fun pieceDirection(): MoveDirection

}
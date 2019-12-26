package com.heshten.chess.core.validator

import com.heshten.chess.core.models.PieceSide

class SideMoveValidator {

    private var currentSide = PieceSide.WHITE

    fun getCurrentSide() = currentSide

    fun changeSide() {
        currentSide = if (currentSide == PieceSide.WHITE) {
            PieceSide.BLACK
        } else {
            PieceSide.WHITE
        }
    }

}
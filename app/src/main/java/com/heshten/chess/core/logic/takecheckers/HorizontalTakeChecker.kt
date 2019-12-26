package com.heshten.chess.core.logic.takecheckers

import com.heshten.chess.core.logic.TakeChecker
import com.heshten.chess.core.models.BoardPosition
import com.heshten.chess.core.models.pieces.Piece

class HorizontalTakeChecker : TakeChecker {

    override fun getPossibleTakes(piece: Piece): Set<BoardPosition> {
        return getPossibleHorizontalTakes()
    }

    private fun getPossibleHorizontalTakes(): Set<BoardPosition> {
        val possibleTakeMoves = mutableSetOf<BoardPosition>()

        return possibleTakeMoves
    }

}
package com.heshten.chess.core.logic.takecheckers

import com.heshten.chess.core.logic.TakeChecker
import com.heshten.chess.core.models.BoardPosition
import com.heshten.chess.core.models.pieces.Piece

class KnightLikeTakeChecker : TakeChecker {

    override fun getPossibleTakes(piece: Piece): Set<BoardPosition> {
        return getPossibleKnightLikeTakes()
    }

    private fun getPossibleKnightLikeTakes(): Set<BoardPosition> {
        val possibleTakeMoves = mutableSetOf<BoardPosition>()

        return possibleTakeMoves
    }
}
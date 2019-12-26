package com.heshten.chess.core.logic.takecheckers

import com.heshten.chess.core.logic.TakeChecker
import com.heshten.chess.core.models.BoardPosition
import com.heshten.chess.core.models.pieces.Piece

class DiagonalTakeChecker : TakeChecker {

    override fun getPossibleTakes(piece: Piece): Set<BoardPosition> {
        return getPossibleDiagonalTakeMoves()
    }

    private fun getPossibleDiagonalTakeMoves(): Set<BoardPosition> {
        val possibleTakeMoves = mutableSetOf<BoardPosition>()

        return possibleTakeMoves
    }
}
package com.heshten.chess.core.logic

import com.heshten.chess.core.models.BoardPosition
import com.heshten.chess.core.models.pieces.Piece

interface MoveChecker {

    fun getPossibleMoves(piece: Piece): Set<BoardPosition>

}
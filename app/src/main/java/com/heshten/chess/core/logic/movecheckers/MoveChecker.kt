package com.heshten.chess.core.logic.movecheckers

import com.heshten.chess.core.models.BoardPosition
import com.heshten.chess.core.models.pieces.Piece

abstract class MoveChecker {

    protected fun hasPieceOnPosition(boardPosition: BoardPosition, pieces: Set<Piece>): Boolean {
        return pieces.find { it.getCurrentPosition() == boardPosition } != null
    }

    abstract fun getPossibleMoves(piece: Piece, boardPieces: Set<Piece>): Set<BoardPosition>

}
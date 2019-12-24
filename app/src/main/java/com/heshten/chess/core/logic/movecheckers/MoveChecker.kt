package com.heshten.chess.core.logic.movecheckers

import com.heshten.chess.core.logic.Board
import com.heshten.chess.core.models.BoardPosition
import com.heshten.chess.core.models.pieces.Piece

abstract class MoveChecker(protected val board: Board) {

    protected fun isDifferent(targetPiece: Piece, nextPosition: BoardPosition): Boolean {
        return board.getPieceForPosition(nextPosition)?.isDifferentSidePieces(targetPiece) == true
    }

    abstract fun getPossibleMoves(piece: Piece): Set<BoardPosition>

}
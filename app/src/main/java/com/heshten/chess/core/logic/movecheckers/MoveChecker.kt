package com.heshten.chess.core.logic.movecheckers

import com.heshten.chess.core.logic.Board
import com.heshten.chess.core.models.BoardPosition
import com.heshten.chess.core.models.pieces.Piece

abstract class MoveChecker(protected val board: Board) {

    abstract fun getPossibleMoves(piece: Piece): Set<BoardPosition>

}
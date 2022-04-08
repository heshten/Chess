package com.heshten.core.logic

import com.heshten.core.models.BoardPosition
import com.heshten.core.models.pieces.Piece

interface MoveChecker {

  fun getPossibleMoves(piece: Piece): Set<BoardPosition>
}

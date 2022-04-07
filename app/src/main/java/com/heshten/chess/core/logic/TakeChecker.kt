package com.heshten.chess.core.logic

import com.heshten.chess.core.models.BoardPosition
import com.heshten.chess.core.models.pieces.Piece

interface TakeChecker {

  fun getPossibleTakes(piece: Piece): Set<BoardPosition>
}

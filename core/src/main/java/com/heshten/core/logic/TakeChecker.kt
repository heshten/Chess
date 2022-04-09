package com.heshten.core.logic

import com.heshten.core.board.ChessBoard
import com.heshten.core.models.BoardPosition
import com.heshten.core.models.pieces.Piece

interface TakeChecker {

  fun getPossibleTakes(piece: Piece, chessBoard: ChessBoard): Set<BoardPosition>
}

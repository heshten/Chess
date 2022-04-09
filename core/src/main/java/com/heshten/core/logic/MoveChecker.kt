package com.heshten.core.logic

import com.heshten.core.board.ChessBoard
import com.heshten.core.models.BoardPosition
import com.heshten.core.models.pieces.Piece

interface MoveChecker {

  fun getPossibleMoves(piece: Piece, chessBoard: ChessBoard): Set<BoardPosition>
}

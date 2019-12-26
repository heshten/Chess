package com.heshten.chess.ui.views

import com.heshten.chess.core.board.ChessBoard

interface IBoardView {

    fun redrawChessBoard(chessBoard: ChessBoard)

}
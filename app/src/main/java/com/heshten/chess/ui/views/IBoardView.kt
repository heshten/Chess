package com.heshten.chess.ui.views

import com.heshten.chess.core.models.BoardPosition
import com.heshten.chess.core.models.pieces.Piece

interface IBoardView {

    fun setPieces(pieces: Set<Piece>)
    fun setSelectedPositions(positions: Set<BoardPosition>)

}
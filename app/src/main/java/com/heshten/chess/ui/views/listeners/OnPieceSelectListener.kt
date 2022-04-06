package com.heshten.chess.ui.views.listeners

import com.heshten.chess.core.models.BoardPosition
import com.heshten.chess.core.models.pieces.Piece

interface OnPieceSelectListener {

  fun onPieceSelected(piece: Piece)

  fun moveSelectedPieceToPosition(boardPosition: BoardPosition)
}

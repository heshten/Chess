package com.heshten.core.models

import com.heshten.core.models.pieces.Piece

sealed class Move {

  data class Regular(
    val piece: Piece,
    val fromPosition: BoardPosition,
    val toPosition: BoardPosition
  ) : Move()

  data class Castling(
    val kingMove: Regular,
    val rookMove: Regular
  ) : Move()
}

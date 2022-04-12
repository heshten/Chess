package com.heshten.core.models

import com.heshten.core.models.pieces.Piece

data class Move(
  val piece: Piece,
  val fromPosition: BoardPosition,
  val toPosition: BoardPosition
)

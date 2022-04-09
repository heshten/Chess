package com.heshten.core.models

enum class PieceSide {
  WHITE, BLACK
}

fun PieceSide.opposite(): PieceSide = when (this) {
  PieceSide.WHITE -> PieceSide.BLACK
  PieceSide.BLACK -> PieceSide.WHITE
}

package com.heshten.core.validator

import com.heshten.core.models.PieceSide

class SideMoveValidator {

  private var currentSide = PieceSide.WHITE

  fun getCurrentSide() = currentSide

  fun changeSide(): PieceSide {
    currentSide = if (currentSide == PieceSide.WHITE) {
      PieceSide.BLACK
    } else {
      PieceSide.WHITE
    }
    return getCurrentSide()
  }
}

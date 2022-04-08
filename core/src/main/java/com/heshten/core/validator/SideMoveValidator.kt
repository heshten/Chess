package com.heshten.core.validator

import com.heshten.core.models.PieceSide

class SideMoveValidator(private val onSideChanged: (PieceSide) -> Unit) {

  private var currentSide = PieceSide.WHITE

  init {
    notifySideChanged()
  }

  fun getCurrentSide() = currentSide

  fun changeSide() {
    currentSide = if (currentSide == PieceSide.WHITE) {
      PieceSide.BLACK
    } else {
      PieceSide.WHITE
    }
    notifySideChanged()
  }

  private fun notifySideChanged() {
    onSideChanged.invoke(getCurrentSide())
  }
}

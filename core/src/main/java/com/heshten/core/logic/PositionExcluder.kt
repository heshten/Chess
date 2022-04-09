package com.heshten.core.logic

import com.heshten.core.models.BoardPosition

object PositionExcluder {

  fun excludePositionsOutOfBoardInPlace(target: MutableSet<BoardPosition>) {
    target.removeAll { !positionIsOnBoard(it) }
  }

  private fun positionIsOnBoard(position: BoardPosition): Boolean {
    return position.columnIndex in 0..7 && position.rowIndex in 0..7
  }
}

package com.heshten.core.logic

import com.heshten.core.models.BoardPosition

object PositionExclude {

  /**
   * Workaround method. Must be removed.
   * */
  fun excludePositionsOutOfBoardInPlace(target: MutableSet<BoardPosition>) {
    target.removeAll { !positionIsOnBoard(it) }
  }

  private fun positionIsOnBoard(position: BoardPosition): Boolean {
    return position.columnIndex in 0..7 && position.rowIndex in 0..7
  }
}

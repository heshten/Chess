package com.heshten.core.logic.facedes

import com.heshten.core.logic.TakeChecker
import com.heshten.core.models.BoardPosition
import com.heshten.core.models.pieces.Piece

class TakeCheckerFacade(
  private val diagonalChecker: TakeChecker,
  private val horizontalChecker: TakeChecker,
  private val knightLikeChecker: TakeChecker,
  private val verticalChecker: TakeChecker
) : TakeChecker {

  override fun getPossibleTakes(piece: Piece): Set<BoardPosition> {
    val possibleTakes = mutableSetOf<BoardPosition>()
    if (piece.canTakeDiagonally()) {
      possibleTakes.addAll(diagonalChecker.getPossibleTakes(piece))
    }
    if (piece.canTakeHorizontally()) {
      possibleTakes.addAll(horizontalChecker.getPossibleTakes(piece))
    }
    if (piece.canTakeVertically()) {
      possibleTakes.addAll(verticalChecker.getPossibleTakes(piece))
    }
    if (piece.canTakeKnightLike()) {
      possibleTakes.addAll(knightLikeChecker.getPossibleTakes(piece))
    }
    return possibleTakes
  }
}
package com.heshten.core.logic.facedes

import com.heshten.core.board.ChessBoard
import com.heshten.core.logic.MoveChecker
import com.heshten.core.models.BoardPosition
import com.heshten.core.models.pieces.Piece

class MoveCheckerFacade(
  private val horizontalChecker: MoveChecker,
  private val verticalChecker: MoveChecker,
  private val diagonalChecker: MoveChecker,
  private val knightLikeChecker: MoveChecker
) : MoveChecker {

  override fun getPossibleMoves(piece: Piece, chessBoard: ChessBoard): Set<BoardPosition> {
    val possibleMoves = mutableSetOf<BoardPosition>()
    if (piece.canMoveDiagonally()) {
      possibleMoves.addAll(diagonalChecker.getPossibleMoves(piece, chessBoard))
    }
    if (piece.canMoveHorizontally()) {
      possibleMoves.addAll(horizontalChecker.getPossibleMoves(piece, chessBoard))
    }
    if (piece.canMoveVertically()) {
      possibleMoves.addAll(verticalChecker.getPossibleMoves(piece, chessBoard))
    }
    if (piece.canMoveKnightLike()) {
      possibleMoves.addAll(knightLikeChecker.getPossibleMoves(piece, chessBoard))
    }
    return possibleMoves
  }
}

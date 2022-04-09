package com.heshten.core

import com.heshten.core.models.BoardPosition
import com.heshten.core.models.Direction
import com.heshten.core.models.PieceSide
import com.heshten.core.models.pieces.*

class NewGameBoardCreator {

  fun createNewBoard(topSide: PieceSide, bottomSide: PieceSide): Set<Piece> {
    val pieces = mutableSetOf<Piece>()
    val topPieces = createTopPieces(topSide)
    val bottomPieces = createBottomPieces(bottomSide)
    pieces.addAll(topPieces)
    pieces.addAll(bottomPieces)
    return pieces
  }

  private fun createTopPieces(side: PieceSide): Set<Piece> {
    return setOf(
      Rook(side, Direction.DOWN, BoardPosition(0, 0), false),
      Rook(side, Direction.DOWN, BoardPosition(0, 7), false),
      Knight(side, Direction.DOWN, BoardPosition(0, 1), false),
      Knight(side, Direction.DOWN, BoardPosition(0, 6), false),
      Bishop(side, Direction.DOWN, BoardPosition(0, 2), false),
      Bishop(side, Direction.DOWN, BoardPosition(0, 5), false),
      King(side, Direction.DOWN, BoardPosition(0, 3), false),
      Queen(side, Direction.DOWN, BoardPosition(0, 4), false),
      //pawns
      Pawn(side, Direction.DOWN, BoardPosition(1, 0), false),
      Pawn(side, Direction.DOWN, BoardPosition(1, 1), false),
      Pawn(side, Direction.DOWN, BoardPosition(1, 2), false),
      Pawn(side, Direction.DOWN, BoardPosition(1, 3), false),
      Pawn(side, Direction.DOWN, BoardPosition(1, 4), false),
      Pawn(side, Direction.DOWN, BoardPosition(1, 5), false),
      Pawn(side, Direction.DOWN, BoardPosition(1, 6), false),
      Pawn(side, Direction.DOWN, BoardPosition(1, 7), false)
    )
  }

  private fun createBottomPieces(side: PieceSide): Set<Piece> {
    return setOf(
      Rook(side, Direction.UP, BoardPosition(7, 0), false),
      Rook(side, Direction.UP, BoardPosition(7, 7), false),
      Knight(side, Direction.UP, BoardPosition(7, 1), false),
      Knight(side, Direction.UP, BoardPosition(7, 6), false),
      Bishop(side, Direction.UP, BoardPosition(7, 2), false),
      Bishop(side, Direction.UP, BoardPosition(7, 5), false),
      King(side, Direction.UP, BoardPosition(7, 3), false),
      Queen(side, Direction.UP, BoardPosition(7, 4), false),
      //pawns
      Pawn(side, Direction.UP, BoardPosition(6, 0), false),
      Pawn(side, Direction.UP, BoardPosition(6, 1), false),
      Pawn(side, Direction.UP, BoardPosition(6, 2), false),
      Pawn(side, Direction.UP, BoardPosition(6, 3), false),
      Pawn(side, Direction.UP, BoardPosition(6, 4), false),
      Pawn(side, Direction.UP, BoardPosition(6, 5), false),
      Pawn(side, Direction.UP, BoardPosition(6, 6), false),
      Pawn(side, Direction.UP, BoardPosition(6, 7), false)
    )
  }
}

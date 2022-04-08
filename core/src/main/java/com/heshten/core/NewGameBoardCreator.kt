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
      Rook(side, Direction.DOWN, BoardPosition(0, 0)),
      Rook(side, Direction.DOWN, BoardPosition(0, 7)),
      Knight(side, Direction.DOWN, BoardPosition(0, 1)),
      Knight(side, Direction.DOWN, BoardPosition(0, 6)),
      Bishop(side, Direction.DOWN, BoardPosition(0, 2)),
      Bishop(side, Direction.DOWN, BoardPosition(0, 5)),
      King(side, Direction.DOWN, BoardPosition(0, 3)),
      Queen(side, Direction.DOWN, BoardPosition(0, 4)),
      //pawns
      Pawn(side, Direction.DOWN, BoardPosition(1, 0)),
      Pawn(side, Direction.DOWN, BoardPosition(1, 1)),
      Pawn(side, Direction.DOWN, BoardPosition(1, 2)),
      Pawn(side, Direction.DOWN, BoardPosition(1, 3)),
      Pawn(side, Direction.DOWN, BoardPosition(1, 4)),
      Pawn(side, Direction.DOWN, BoardPosition(1, 5)),
      Pawn(side, Direction.DOWN, BoardPosition(1, 6)),
      Pawn(side, Direction.DOWN, BoardPosition(1, 7))
    )
  }

  private fun createBottomPieces(side: PieceSide): Set<Piece> {
    return setOf(
      Rook(side, Direction.UP, BoardPosition(7, 0)),
      Rook(side, Direction.UP, BoardPosition(7, 7)),
      Knight(side, Direction.UP, BoardPosition(7, 1)),
      Knight(side, Direction.UP, BoardPosition(7, 6)),
      Bishop(side, Direction.UP, BoardPosition(7, 2)),
      Bishop(side, Direction.UP, BoardPosition(7, 5)),
      King(side, Direction.UP, BoardPosition(7, 3)),
      Queen(side, Direction.UP, BoardPosition(7, 4)),
      //pawns
      Pawn(side, Direction.UP, BoardPosition(6, 0)),
      Pawn(side, Direction.UP, BoardPosition(6, 1)),
      Pawn(side, Direction.UP, BoardPosition(6, 2)),
      Pawn(side, Direction.UP, BoardPosition(6, 3)),
      Pawn(side, Direction.UP, BoardPosition(6, 4)),
      Pawn(side, Direction.UP, BoardPosition(6, 5)),
      Pawn(side, Direction.UP, BoardPosition(6, 6)),
      Pawn(side, Direction.UP, BoardPosition(6, 7))
    )
  }
}

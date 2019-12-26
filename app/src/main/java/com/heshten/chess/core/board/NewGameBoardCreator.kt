package com.heshten.chess.core.board

import com.heshten.chess.core.models.BoardPosition
import com.heshten.chess.core.models.Direction
import com.heshten.chess.core.models.PieceSide
import com.heshten.chess.core.models.pieces.*
import com.heshten.chess.core.recources.PieceResourceProvider

class NewGameBoardCreator(
    private val whitePiecesResourceProvider: PieceResourceProvider,
    private val blackPiecesResourceProvider: PieceResourceProvider
) {

    fun createNewBoard(topSide: PieceSide, bottomSide: PieceSide): MutableSet<Piece> {
        val pieces = mutableSetOf<Piece>()
        val topPieces = createTopPieces(topSide, getResourceProviderForSide(topSide))
        val bottomPieces = createBottomPieces(bottomSide, getResourceProviderForSide(bottomSide))
        pieces.addAll(topPieces)
        pieces.addAll(bottomPieces)
        return pieces
    }

    private fun createTopPieces(
        side: PieceSide,
        resourceProvider: PieceResourceProvider
    ): Set<Piece> {
        return setOf(
            Rook(resourceProvider.getRookBitmap(), side, Direction.DOWN, BoardPosition(0, 0)),
            Rook(resourceProvider.getRookBitmap(), side, Direction.DOWN, BoardPosition(0, 7)),
            Knight(resourceProvider.getKnightBitmap(), side, Direction.DOWN, BoardPosition(0, 1)),
            Knight(resourceProvider.getKnightBitmap(), side, Direction.DOWN, BoardPosition(0, 6)),
            Bishop(resourceProvider.getBishopBitmap(), side, Direction.DOWN, BoardPosition(0, 2)),
            Bishop(resourceProvider.getBishopBitmap(), side, Direction.DOWN, BoardPosition(0, 5)),
            King(resourceProvider.getKingBitmap(), side, Direction.DOWN, BoardPosition(0, 3)),
            Queen(resourceProvider.getQueenBitmap(), side, Direction.DOWN, BoardPosition(0, 4)),
            //pawns
            Pawn(resourceProvider.getPawnBitmap(), side, Direction.DOWN, BoardPosition(1, 0)),
            Pawn(resourceProvider.getPawnBitmap(), side, Direction.DOWN, BoardPosition(1, 1)),
            Pawn(resourceProvider.getPawnBitmap(), side, Direction.DOWN, BoardPosition(1, 2)),
            Pawn(resourceProvider.getPawnBitmap(), side, Direction.DOWN, BoardPosition(1, 3)),
            Pawn(resourceProvider.getPawnBitmap(), side, Direction.DOWN, BoardPosition(1, 4)),
            Pawn(resourceProvider.getPawnBitmap(), side, Direction.DOWN, BoardPosition(1, 5)),
            Pawn(resourceProvider.getPawnBitmap(), side, Direction.DOWN, BoardPosition(1, 6)),
            Pawn(resourceProvider.getPawnBitmap(), side, Direction.DOWN, BoardPosition(1, 7))
        )
    }

    private fun createBottomPieces(
        side: PieceSide,
        resourceProvider: PieceResourceProvider
    ): Set<Piece> {
        return setOf(
            Rook(resourceProvider.getRookBitmap(), side, Direction.UP, BoardPosition(7, 0)),
            Rook(resourceProvider.getRookBitmap(), side, Direction.UP, BoardPosition(7, 7)),
            Knight(resourceProvider.getKnightBitmap(), side, Direction.UP, BoardPosition(7, 1)),
            Knight(resourceProvider.getKnightBitmap(), side, Direction.UP, BoardPosition(7, 6)),
            Bishop(resourceProvider.getBishopBitmap(), side, Direction.UP, BoardPosition(7, 2)),
            Bishop(resourceProvider.getBishopBitmap(), side, Direction.UP, BoardPosition(7, 5)),
            King(resourceProvider.getKingBitmap(), side, Direction.UP, BoardPosition(7, 3)),
            Queen(resourceProvider.getQueenBitmap(), side, Direction.UP, BoardPosition(7, 4)),
            //pawns
            Pawn(resourceProvider.getPawnBitmap(), side, Direction.UP, BoardPosition(6, 0)),
            Pawn(resourceProvider.getPawnBitmap(), side, Direction.UP, BoardPosition(6, 1)),
            Pawn(resourceProvider.getPawnBitmap(), side, Direction.UP, BoardPosition(6, 2)),
            Pawn(resourceProvider.getPawnBitmap(), side, Direction.UP, BoardPosition(6, 3)),
            Pawn(resourceProvider.getPawnBitmap(), side, Direction.UP, BoardPosition(6, 4)),
            Pawn(resourceProvider.getPawnBitmap(), side, Direction.UP, BoardPosition(6, 5)),
            Pawn(resourceProvider.getPawnBitmap(), side, Direction.UP, BoardPosition(6, 6)),
            Pawn(resourceProvider.getPawnBitmap(), side, Direction.UP, BoardPosition(6, 7))
        )
    }

    private fun getResourceProviderForSide(side: PieceSide) = when (side) {
        PieceSide.WHITE -> whitePiecesResourceProvider
        PieceSide.BLACK -> blackPiecesResourceProvider
    }

}
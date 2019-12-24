package com.heshten.chess.core.logic

import com.heshten.chess.core.models.BoardPosition
import com.heshten.chess.core.models.helpers.MoveDirection
import com.heshten.chess.core.models.helpers.PieceSide
import com.heshten.chess.core.models.pieces.*
import com.heshten.chess.core.recources.PieceResourceProvider

class Board(private val whitePiecesResourceProvider: PieceResourceProvider,
            private val blackPiecesResourceProvider: PieceResourceProvider) {

    private var selectedPiece: Piece? = null

    val pieces: Set<Piece> = createNewBoardPositions()
    val selectedPositions = mutableSetOf<BoardPosition>()

    fun hasPieceAtPosition(boardPosition: BoardPosition): Boolean {
        return pieces.find { it.getCurrentPosition() == boardPosition } != null
    }

    fun moveSelectedPieceToPosition(boardPosition: BoardPosition) {
        selectedPiece?.moveTo(boardPosition)
        selectedPiece = null
    }

    fun selectPiece(piece: Piece) {
        selectedPiece = piece
    }

    fun setSelectedPositions(positions: Set<BoardPosition>) {
        clearSelectedPositions()
        selectedPositions.addAll(positions)
    }

    fun clearSelectedPositions() {
        selectedPositions.clear()
    }

    private fun createNewBoardPositions() = setOf(
        //white pieces
        Rook(whitePiecesResourceProvider.getRookBitmap(), PieceSide.WHITE, BoardPosition(0, 0)),
        Rook(whitePiecesResourceProvider.getRookBitmap(), PieceSide.WHITE, BoardPosition(0, 7)),
        Knight(whitePiecesResourceProvider.getKnightBitmap(), PieceSide.WHITE, BoardPosition(0, 1)),
        Knight(whitePiecesResourceProvider.getKnightBitmap(), PieceSide.WHITE, BoardPosition(0, 6)),
        Bishop(whitePiecesResourceProvider.getBishopBitmap(), PieceSide.WHITE, BoardPosition(0, 2)),
        Bishop(whitePiecesResourceProvider.getBishopBitmap(), PieceSide.WHITE, BoardPosition(0, 5)),
        King(whitePiecesResourceProvider.getKingBitmap(), PieceSide.WHITE, BoardPosition(0, 3)),
        Queen(whitePiecesResourceProvider.getQueenBitmap(), PieceSide.WHITE,  BoardPosition(0, 4)),

        Pawn(whitePiecesResourceProvider.getPawnBitmap(), BoardPosition(1, 0), PieceSide.WHITE, MoveDirection.DOWN),
        Pawn(whitePiecesResourceProvider.getPawnBitmap(), BoardPosition(1, 1), PieceSide.WHITE, MoveDirection.DOWN),
        Pawn(whitePiecesResourceProvider.getPawnBitmap(), BoardPosition(1, 2), PieceSide.WHITE, MoveDirection.DOWN),
        Pawn(whitePiecesResourceProvider.getPawnBitmap(), BoardPosition(1, 3), PieceSide.WHITE, MoveDirection.DOWN),
        Pawn(whitePiecesResourceProvider.getPawnBitmap(), BoardPosition(1, 4), PieceSide.WHITE, MoveDirection.DOWN),
        Pawn(whitePiecesResourceProvider.getPawnBitmap(), BoardPosition(1, 5), PieceSide.WHITE, MoveDirection.DOWN),
        Pawn(whitePiecesResourceProvider.getPawnBitmap(), BoardPosition(1, 6), PieceSide.WHITE, MoveDirection.DOWN),
        Pawn(whitePiecesResourceProvider.getPawnBitmap(), BoardPosition(1, 7), PieceSide.WHITE, MoveDirection.DOWN),

        //black pieces
        Pawn(blackPiecesResourceProvider.getPawnBitmap(), BoardPosition(6, 0), PieceSide.BLACK, MoveDirection.UP),
        Pawn(blackPiecesResourceProvider.getPawnBitmap(), BoardPosition(6, 1), PieceSide.BLACK, MoveDirection.UP),
        Pawn(blackPiecesResourceProvider.getPawnBitmap(), BoardPosition(6, 2), PieceSide.BLACK, MoveDirection.UP),
        Pawn(blackPiecesResourceProvider.getPawnBitmap(), BoardPosition(6, 3), PieceSide.BLACK, MoveDirection.UP),
        Pawn(blackPiecesResourceProvider.getPawnBitmap(), BoardPosition(6, 4), PieceSide.BLACK, MoveDirection.UP),
        Pawn(blackPiecesResourceProvider.getPawnBitmap(), BoardPosition(6, 5), PieceSide.BLACK, MoveDirection.UP),
        Pawn(blackPiecesResourceProvider.getPawnBitmap(), BoardPosition(6, 6), PieceSide.BLACK, MoveDirection.UP),
        Pawn(blackPiecesResourceProvider.getPawnBitmap(), BoardPosition(6, 7), PieceSide.BLACK, MoveDirection.UP),

        Rook(blackPiecesResourceProvider.getRookBitmap(), PieceSide.BLACK, BoardPosition(7, 0)),
        Rook(blackPiecesResourceProvider.getRookBitmap(), PieceSide.BLACK, BoardPosition(7, 7)),
        Knight(blackPiecesResourceProvider.getKnightBitmap(), PieceSide.BLACK, BoardPosition(7, 1)),
        Knight(blackPiecesResourceProvider.getKnightBitmap(), PieceSide.BLACK, BoardPosition(7, 6)),
        Bishop(blackPiecesResourceProvider.getBishopBitmap(), PieceSide.BLACK, BoardPosition(7, 2)),
        Bishop(blackPiecesResourceProvider.getBishopBitmap(), PieceSide.BLACK, BoardPosition(7, 5)),
        King(blackPiecesResourceProvider.getKingBitmap(), PieceSide.BLACK, BoardPosition(7, 3)),
        Queen(blackPiecesResourceProvider.getQueenBitmap(), PieceSide.BLACK, BoardPosition(7, 4))
    )

}
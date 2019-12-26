package com.heshten.chess.core

import com.heshten.chess.core.models.BoardPosition
import com.heshten.chess.core.models.PieceSide
import com.heshten.chess.core.models.pieces.Piece

class ChessBoard(
    topSide: PieceSide,
    bottomSide: PieceSide,
    newGameBoardCreator: NewGameBoardCreator
) {

    private var selectedPiece: Piece? = null

    val selectedPositions = mutableSetOf<BoardPosition>()
    val pieces: Set<Piece> = newGameBoardCreator.createNewBoard(topSide, bottomSide)

    fun hasPieceAtPosition(boardPosition: BoardPosition): Boolean {
        return getPieceForPosition(boardPosition) != null
    }

    fun getPieceForPosition(boardPosition: BoardPosition): Piece? {
        return pieces.find { it.getCurrentPosition() == boardPosition }
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

}
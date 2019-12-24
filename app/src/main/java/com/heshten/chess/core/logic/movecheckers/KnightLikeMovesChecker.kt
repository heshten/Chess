package com.heshten.chess.core.logic.movecheckers

import com.heshten.chess.core.models.BoardPosition
import com.heshten.chess.core.models.pieces.Piece

class KnightLikeMovesChecker : MoveChecker() {

    override fun getPossibleMoves(piece: Piece, boardPieces: Set<Piece>): Set<BoardPosition> {
        if (!piece.canMoveKnightLike()) {
            return emptySet()
        }
        return getKnightLikePossibleMoves(piece, boardPieces)
    }

    private fun getKnightLikePossibleMoves(piece: Piece, boardPieces: Set<Piece>): Set<BoardPosition> {
        val possiblePositions = mutableSetOf<BoardPosition>()
        val startRowIndex = piece.getCurrentPosition().rowIndex
        val startColumnIndex = piece.getCurrentPosition().columnIndex
        val upLeftPosition = BoardPosition(startRowIndex - 2, startColumnIndex - 1)
        val upRightPosition = BoardPosition(startRowIndex - 2, startColumnIndex + 1)
        val leftUpPosition = BoardPosition(startRowIndex - 1, startColumnIndex - 2)
        val leftDownPosition = BoardPosition(startRowIndex - 1, startColumnIndex + 2)
        val rightUpPosition = BoardPosition(startRowIndex + 1, startColumnIndex - 2)
        val rightDownPosition = BoardPosition(startRowIndex + 1, startColumnIndex + 2)
        val bottomLeftPosition = BoardPosition(startRowIndex + 2, startColumnIndex - 1)
        val bottomRightPosition = BoardPosition(startRowIndex + 2, startColumnIndex + 1)
        maybeAddPossiblePosition(upLeftPosition, possiblePositions, boardPieces)
        maybeAddPossiblePosition(upRightPosition, possiblePositions, boardPieces)
        maybeAddPossiblePosition(leftUpPosition, possiblePositions, boardPieces)
        maybeAddPossiblePosition(leftDownPosition, possiblePositions, boardPieces)
        maybeAddPossiblePosition(rightUpPosition, possiblePositions, boardPieces)
        maybeAddPossiblePosition(rightDownPosition, possiblePositions, boardPieces)
        maybeAddPossiblePosition(bottomLeftPosition, possiblePositions, boardPieces)
        maybeAddPossiblePosition(bottomRightPosition, possiblePositions, boardPieces)
        return possiblePositions
    }

    private fun maybeAddPossiblePosition(
        possiblePosition: BoardPosition,
        possibleMovesContainer: MutableSet<BoardPosition>,
        boardPieces: Set<Piece>
    ) {
        if (!hasPieceOnPosition(possiblePosition, boardPieces)) {
            possibleMovesContainer.add(possiblePosition)
        }
    }

}
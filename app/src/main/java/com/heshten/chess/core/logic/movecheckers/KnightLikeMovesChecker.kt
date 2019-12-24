package com.heshten.chess.core.logic.movecheckers

import com.heshten.chess.core.logic.Board
import com.heshten.chess.core.models.BoardPosition
import com.heshten.chess.core.models.pieces.Piece

class KnightLikeMovesChecker(board: Board) : MoveChecker(board) {

    override fun getPossibleMoves(piece: Piece): Set<BoardPosition> {
        if (!piece.canMoveKnightLike()) {
            return emptySet()
        }
        return getKnightLikePossibleMoves(piece)
    }

    private fun getKnightLikePossibleMoves(piece: Piece): Set<BoardPosition> {
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
        maybeAddPossiblePosition(upLeftPosition, possiblePositions)
        maybeAddPossiblePosition(upRightPosition, possiblePositions)
        maybeAddPossiblePosition(leftUpPosition, possiblePositions)
        maybeAddPossiblePosition(leftDownPosition, possiblePositions)
        maybeAddPossiblePosition(rightUpPosition, possiblePositions)
        maybeAddPossiblePosition(rightDownPosition, possiblePositions)
        maybeAddPossiblePosition(bottomLeftPosition, possiblePositions)
        maybeAddPossiblePosition(bottomRightPosition, possiblePositions)
        return possiblePositions
    }

    private fun maybeAddPossiblePosition(
        possiblePosition: BoardPosition,
        possibleMovesContainer: MutableSet<BoardPosition>
    ) {
        if (!board.hasPieceAtPosition(possiblePosition)) {
            possibleMovesContainer.add(possiblePosition)
        }
    }

}
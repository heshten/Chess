package com.heshten.chess.core.logic.movecheckers

import com.heshten.chess.core.models.BoardPosition
import com.heshten.chess.core.models.pieces.Piece

class HorizontalMovesChecker : MoveChecker() {

    override fun getPossibleMoves(piece: Piece, boardPieces: Set<Piece>): Set<BoardPosition> {
        if (!piece.canMoveHorizontally()) {
            return emptySet()
        }
        return getPossibleHorizontalMoves(piece, boardPieces)
    }

    private fun getPossibleHorizontalMoves(
        piece: Piece,
        boardPieces: Set<Piece>
    ): Set<BoardPosition> {
        val possibleMoves = mutableSetOf<BoardPosition>()
        val rightSideMoves = getRightSidePossibleMoves(piece, boardPieces)
        val leftSideMoves = getLeftSidePossibleMoves(piece, boardPieces)
        possibleMoves.addAll(rightSideMoves)
        possibleMoves.addAll(leftSideMoves)
        return possibleMoves
    }

    private fun getRightSidePossibleMoves(
        piece: Piece,
        boardPieces: Set<Piece>
    ): Set<BoardPosition> {
        val possibleMoves = mutableSetOf<BoardPosition>()
        val rowIndex = piece.getCurrentPosition().rowIndex
        val startColumnIndex = piece.getCurrentPosition().columnIndex + 1
        (startColumnIndex until 8).forEachIndexed { step, columnIndex ->
            if (step >= piece.maxSteps()) {
                return possibleMoves
            }
            val nextBoardPosition = BoardPosition(rowIndex, columnIndex)
            if (!hasPieceOnPosition(nextBoardPosition, boardPieces)) {
                possibleMoves.add(nextBoardPosition)
            } else {
                return possibleMoves
            }
        }
        return possibleMoves
    }

    private fun getLeftSidePossibleMoves(
        piece: Piece,
        boardPieces: Set<Piece>
    ): Set<BoardPosition> {
        val possibleMoves = mutableSetOf<BoardPosition>()
        val rowIndex = piece.getCurrentPosition().rowIndex
        val startColumnIndex = piece.getCurrentPosition().columnIndex - 1
        (startColumnIndex downTo 0).forEachIndexed { step, columnIndex ->
            if (step >= piece.maxSteps()) {
                return possibleMoves
            }
            val nextBoardPosition = BoardPosition(rowIndex, columnIndex)
            if (!hasPieceOnPosition(nextBoardPosition, boardPieces)) {
                possibleMoves.add(nextBoardPosition)
            } else {
                return possibleMoves
            }
        }
        return possibleMoves
    }

}
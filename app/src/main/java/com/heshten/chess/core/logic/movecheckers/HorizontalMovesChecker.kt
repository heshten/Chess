package com.heshten.chess.core.logic.movecheckers

import com.heshten.chess.core.logic.Board
import com.heshten.chess.core.models.BoardPosition
import com.heshten.chess.core.models.pieces.Piece

class HorizontalMovesChecker(board: Board) : MoveChecker(board) {

    override fun getPossibleMoves(piece: Piece): Set<BoardPosition> {
        if (!piece.canMoveHorizontally()) {
            return emptySet()
        }
        return getPossibleHorizontalMoves(piece)
    }

    private fun getPossibleHorizontalMoves(piece: Piece): Set<BoardPosition> {
        val possibleMoves = mutableSetOf<BoardPosition>()
        val rightSideMoves = getRightSidePossibleMoves(piece)
        val leftSideMoves = getLeftSidePossibleMoves(piece)
        possibleMoves.addAll(rightSideMoves)
        possibleMoves.addAll(leftSideMoves)
        return possibleMoves
    }

    private fun getRightSidePossibleMoves(piece: Piece): Set<BoardPosition> {
        val possibleMoves = mutableSetOf<BoardPosition>()
        val rowIndex = piece.getCurrentPosition().rowIndex
        val startColumnIndex = piece.getCurrentPosition().columnIndex + 1
        (startColumnIndex until 8).forEachIndexed { step, columnIndex ->
            if (step >= piece.maxSteps()) {
                return possibleMoves
            }
            val nextBoardPosition = BoardPosition(rowIndex, columnIndex)
            if (!board.hasPieceAtPosition(nextBoardPosition)) {
                possibleMoves.add(nextBoardPosition)
            } else {
                if (isDifferent(piece, nextBoardPosition) && piece.canTakeHorizontally()) {
                    possibleMoves.add(nextBoardPosition)
                }
                return possibleMoves
            }
        }
        return possibleMoves
    }

    private fun getLeftSidePossibleMoves(piece: Piece): Set<BoardPosition> {
        val possibleMoves = mutableSetOf<BoardPosition>()
        val rowIndex = piece.getCurrentPosition().rowIndex
        val startColumnIndex = piece.getCurrentPosition().columnIndex - 1
        (startColumnIndex downTo 0).forEachIndexed { step, columnIndex ->
            if (step >= piece.maxSteps()) {
                return possibleMoves
            }
            val nextBoardPosition = BoardPosition(rowIndex, columnIndex)
            if (!board.hasPieceAtPosition(nextBoardPosition)) {
                possibleMoves.add(nextBoardPosition)
            } else {
                if (isDifferent(piece, nextBoardPosition) && piece.canTakeHorizontally()) {
                    possibleMoves.add(nextBoardPosition)
                }
                return possibleMoves
            }
        }
        return possibleMoves
    }

}
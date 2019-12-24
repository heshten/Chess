package com.heshten.chess.core.logic.movecheckers

import com.heshten.chess.core.logic.Board
import com.heshten.chess.core.models.BoardPosition
import com.heshten.chess.core.models.helpers.MoveDirection
import com.heshten.chess.core.models.pieces.Piece

class VerticalMovesChecker(board: Board) : MoveChecker(board) {

    override fun getPossibleMoves(piece: Piece): Set<BoardPosition> {
        if (!piece.canMoveVertically()) {
            return emptySet()
        }
        return when (piece.pieceDirection()) {
            MoveDirection.UP -> getPossibleVerticalUpMoves(piece)
            MoveDirection.DOWN -> getPossibleVerticalDownMoves(piece)
            MoveDirection.BOTH -> {
                val possibleUpMoves = getPossibleVerticalUpMoves(piece)
                val possibleDownMoves = getPossibleVerticalDownMoves(piece)
                val mergedSet = mutableSetOf<BoardPosition>()
                mergedSet.addAll(possibleUpMoves)
                mergedSet.addAll(possibleDownMoves)
                return mergedSet
            }
        }
    }

    private fun getPossibleVerticalUpMoves(piece: Piece): Set<BoardPosition> {
        val possibleMoves = mutableSetOf<BoardPosition>()
        val startRowPosition = piece.getCurrentPosition().rowIndex - 1
        val columnIndex = piece.getCurrentPosition().columnIndex
        (startRowPosition downTo 0).forEachIndexed { step, rowIndex ->
            if (step >= piece.maxSteps()) {
                return possibleMoves
            }
            val nextVerticalPosition = BoardPosition(rowIndex, columnIndex)
            if (!board.hasPieceAtPosition(nextVerticalPosition)) {
                possibleMoves.add(nextVerticalPosition)
            } else {
                if (isDifferent(piece, nextVerticalPosition) && piece.canTakeVertically()) {
                    possibleMoves.add(nextVerticalPosition)
                }
                return possibleMoves
            }
        }
        return possibleMoves
    }

    private fun getPossibleVerticalDownMoves(piece: Piece): Set<BoardPosition> {
        val possibleMoves = mutableSetOf<BoardPosition>()
        val startRowPosition = piece.getCurrentPosition().rowIndex + 1
        val columnIndex = piece.getCurrentPosition().columnIndex
        (startRowPosition until 8).forEachIndexed { step, rowIndex ->
            if (step >= piece.maxSteps()) {
                return possibleMoves
            }
            val nextVerticalPosition = BoardPosition(rowIndex, columnIndex)
            if (!board.hasPieceAtPosition(nextVerticalPosition)) {
                possibleMoves.add(nextVerticalPosition)
            } else {
                if (isDifferent(piece, nextVerticalPosition) && piece.canTakeVertically()) {
                    possibleMoves.add(nextVerticalPosition)
                }
                return possibleMoves
            }
        }
        return possibleMoves
    }

}
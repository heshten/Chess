package com.heshten.chess.core.logic.movecheckers

import com.heshten.chess.core.models.BoardPosition
import com.heshten.chess.core.models.helpers.MoveDirection
import com.heshten.chess.core.models.pieces.Piece

class VerticalMovesChecker : MoveChecker() {

    override fun getPossibleMoves(piece: Piece, boardPieces: Set<Piece>): Set<BoardPosition> {
        if (!piece.canMoveVertically()) {
            return emptySet()
        }
        return when (piece.pieceDirection()) {
            MoveDirection.UP -> getPossibleVerticalUpMoves(piece, boardPieces)
            MoveDirection.DOWN -> getPossibleVerticalDownMoves(piece, boardPieces)
            MoveDirection.BOTH -> {
                val possibleUpMoves = getPossibleVerticalUpMoves(piece, boardPieces)
                val possibleDownMoves = getPossibleVerticalDownMoves(piece, boardPieces)
                val mergedSet = mutableSetOf<BoardPosition>()
                mergedSet.addAll(possibleUpMoves)
                mergedSet.addAll(possibleDownMoves)
                return mergedSet
            }
        }
    }

    private fun getPossibleVerticalUpMoves(
        piece: Piece,
        boardPieces: Set<Piece>
    ): Set<BoardPosition> {
        val possibleMoves = mutableSetOf<BoardPosition>()
        val startRowPosition = piece.getCurrentPosition().rowIndex - 1
        val columnIndex = piece.getCurrentPosition().columnIndex
        (startRowPosition downTo 0).forEachIndexed { step, rowIndex ->
            if (step >= piece.maxSteps()) {
                return possibleMoves
            }
            val nextVerticalPosition = BoardPosition(rowIndex, columnIndex)
            if (!hasPieceOnPosition(nextVerticalPosition, boardPieces)) {
                possibleMoves.add(nextVerticalPosition)
            } else {
                return possibleMoves
            }
        }
        return possibleMoves
    }

    private fun getPossibleVerticalDownMoves(
        piece: Piece,
        boardPieces: Set<Piece>
    ): Set<BoardPosition> {
        val possibleMoves = mutableSetOf<BoardPosition>()
        val startRowPosition = piece.getCurrentPosition().rowIndex + 1
        val columnIndex = piece.getCurrentPosition().columnIndex
        (startRowPosition until 8).forEachIndexed { step, rowIndex ->
            if (step >= piece.maxSteps()) {
                return possibleMoves
            }
            val nextVerticalPosition = BoardPosition(rowIndex, columnIndex)
            if (!hasPieceOnPosition(nextVerticalPosition, boardPieces)) {
                possibleMoves.add(nextVerticalPosition)
            } else {
                return possibleMoves
            }
        }
        return possibleMoves
    }

}
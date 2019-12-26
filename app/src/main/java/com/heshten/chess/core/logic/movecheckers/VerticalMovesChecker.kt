package com.heshten.chess.core.logic.movecheckers

import com.heshten.chess.core.ChessBoard
import com.heshten.chess.core.logic.MoveChecker
import com.heshten.chess.core.models.BoardPosition
import com.heshten.chess.core.models.helpers.MoveDirection
import com.heshten.chess.core.models.pieces.Piece

class VerticalMovesChecker(private val chessBoard: ChessBoard) : MoveChecker {

    override fun getPossibleMoves(piece: Piece): Set<BoardPosition> {
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
            if (!chessBoard.hasPieceAtPosition(nextVerticalPosition)) {
                possibleMoves.add(nextVerticalPosition)
            } else {
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
            if (!chessBoard.hasPieceAtPosition(nextVerticalPosition)) {
                possibleMoves.add(nextVerticalPosition)
            } else {
                return possibleMoves
            }
        }
        return possibleMoves
    }

}
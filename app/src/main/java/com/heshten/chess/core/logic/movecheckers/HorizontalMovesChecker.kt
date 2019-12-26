package com.heshten.chess.core.logic.movecheckers

import com.heshten.chess.core.ChessBoard
import com.heshten.chess.core.logic.MoveChecker
import com.heshten.chess.core.models.BoardPosition
import com.heshten.chess.core.models.pieces.Piece

class HorizontalMovesChecker(private val chessBoard: ChessBoard) : MoveChecker {

    override fun getPossibleMoves(piece: Piece): Set<BoardPosition> {
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
            if (!chessBoard.hasPieceAtPosition(nextBoardPosition)) {
                possibleMoves.add(nextBoardPosition)
            } else {
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
            if (!chessBoard.hasPieceAtPosition(nextBoardPosition)) {
                possibleMoves.add(nextBoardPosition)
            } else {
                return possibleMoves
            }
        }
        return possibleMoves
    }

}
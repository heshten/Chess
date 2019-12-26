package com.heshten.chess.core.logic.movecheckers

import com.heshten.chess.core.ChessBoard
import com.heshten.chess.core.logic.MoveChecker
import com.heshten.chess.core.models.BoardPosition
import com.heshten.chess.core.models.pieces.Piece

class DiagonalMovesChecker(private val chessBoard: ChessBoard) : MoveChecker {

    override fun getPossibleMoves(piece: Piece): Set<BoardPosition> {
        return getPossibleDiagonallyMoves(piece)
    }

    private fun getPossibleDiagonallyMoves(piece: Piece): Set<BoardPosition> {
        val possibleMoves = mutableSetOf<BoardPosition>()
        val leftUpPossibleMoves = getLeftUpPossibleMoves(piece)
        val rightUpPossibleMoves = getRightUpPossibleMoves(piece)
        val leftDownPossibleMoves = getLeftDownPossibleMoves(piece)
        val rightDownPossibleMoves = getRightDownPossibleMoves(piece)
        possibleMoves.addAll(leftUpPossibleMoves)
        possibleMoves.addAll(rightUpPossibleMoves)
        possibleMoves.addAll(leftDownPossibleMoves)
        possibleMoves.addAll(rightDownPossibleMoves)
        return possibleMoves
    }

    private fun getLeftUpPossibleMoves(piece: Piece): Set<BoardPosition> {
        val possibleMoves = mutableSetOf<BoardPosition>()
        val startRowIndex = piece.getCurrentPosition().rowIndex - 1
        val startColumnIndex = piece.getCurrentPosition().columnIndex - 1
        (0 until 8).forEachIndexed { step, shift ->
            if (step >= piece.maxSteps()) {
                return possibleMoves
            }
            val nextValidLeftUpPosition =
                BoardPosition(startRowIndex - shift, startColumnIndex - shift)
            if (!chessBoard.hasPieceAtPosition(nextValidLeftUpPosition)) {
                possibleMoves.add(nextValidLeftUpPosition)
            } else {
                return possibleMoves
            }
        }
        return possibleMoves
    }

    private fun getRightUpPossibleMoves(piece: Piece): Set<BoardPosition> {
        val possibleMoves = mutableSetOf<BoardPosition>()
        val startRowIndex = piece.getCurrentPosition().rowIndex - 1
        val startColumnIndex = piece.getCurrentPosition().columnIndex + 1
        (0 until 8).forEachIndexed { step, shift ->
            if (step >= piece.maxSteps()) {
                return possibleMoves
            }
            val nextValidRightUpPosition =
                BoardPosition(startRowIndex - shift, startColumnIndex + shift)
            if (!chessBoard.hasPieceAtPosition(nextValidRightUpPosition)) {
                possibleMoves.add(nextValidRightUpPosition)
            } else {
                return possibleMoves
            }
        }
        return possibleMoves
    }

    private fun getLeftDownPossibleMoves(piece: Piece): Set<BoardPosition> {
        val possibleMoves = mutableSetOf<BoardPosition>()
        val startRowIndex = piece.getCurrentPosition().rowIndex + 1
        val startColumnIndex = piece.getCurrentPosition().columnIndex - 1
        (0 until 8).forEachIndexed { step, shift ->
            if (step >= piece.maxSteps()) {
                return possibleMoves
            }
            val nextValidLeftDownPosition =
                BoardPosition(startRowIndex + shift, startColumnIndex - shift)
            if (!chessBoard.hasPieceAtPosition(nextValidLeftDownPosition)) {
                possibleMoves.add(nextValidLeftDownPosition)
            } else {
                return possibleMoves
            }
        }
        return possibleMoves
    }

    private fun getRightDownPossibleMoves(piece: Piece): Set<BoardPosition> {
        val possibleMoves = mutableSetOf<BoardPosition>()
        val startRowIndex = piece.getCurrentPosition().rowIndex + 1
        val startColumnIndex = piece.getCurrentPosition().columnIndex + 1
        (0 until 8).forEachIndexed { step, shift ->
            if (step >= piece.maxSteps()) {
                return possibleMoves
            }
            val nextValidRightDownPosition =
                BoardPosition(startRowIndex + shift, startColumnIndex + shift)
            if (!chessBoard.hasPieceAtPosition(nextValidRightDownPosition)) {
                possibleMoves.add(nextValidRightDownPosition)
            } else {
                return possibleMoves
            }
        }
        return possibleMoves
    }

}
package com.heshten.chess.core.logic.movecheckers

import com.heshten.chess.core.models.BoardPosition
import com.heshten.chess.core.models.pieces.Piece

class DiagonalMovesChecker : MoveChecker() {

    override fun getPossibleMoves(piece: Piece, boardPieces: Set<Piece>): Set<BoardPosition> {
        if (!piece.canMoveDiagonally()) {
            return emptySet()
        }
        return getPossibleDiagonallyMoves(piece, boardPieces)
    }

    private fun getPossibleDiagonallyMoves(
        piece: Piece,
        boardPieces: Set<Piece>
    ): Set<BoardPosition> {
        val possibleMoves = mutableSetOf<BoardPosition>()
        val leftUpPossibleMoves = getLeftUpPossibleMoves(piece, boardPieces)
        val rightUpPossibleMoves = getRightUpPossibleMoves(piece, boardPieces)
        val leftDownPossibleMoves = getLeftDownPossibleMoves(piece, boardPieces)
        val rightDownPossibleMoves = getRightDownPossibleMoves(piece, boardPieces)
        possibleMoves.addAll(leftUpPossibleMoves)
        possibleMoves.addAll(rightUpPossibleMoves)
        possibleMoves.addAll(leftDownPossibleMoves)
        possibleMoves.addAll(rightDownPossibleMoves)
        return possibleMoves
    }

    private fun getLeftUpPossibleMoves(
        piece: Piece,
        boardPieces: Set<Piece>
    ): Set<BoardPosition> {
        val possibleMoves = mutableSetOf<BoardPosition>()
        val startRowIndex = piece.getCurrentPosition().rowIndex - 1
        val startColumnIndex = piece.getCurrentPosition().columnIndex - 1
        (0 until 8).forEachIndexed { step, shift ->
            if (step > piece.maxSteps()) {
                return possibleMoves
            }
            val nextValidLeftUpPosition =
                BoardPosition(startRowIndex - shift, startColumnIndex - shift)
            if (!hasPieceOnPosition(nextValidLeftUpPosition, boardPieces)) {
                possibleMoves.add(nextValidLeftUpPosition)
            } else {
                return possibleMoves
            }
        }
        return possibleMoves
    }

    private fun getRightUpPossibleMoves(
        piece: Piece,
        boardPieces: Set<Piece>
    ): Set<BoardPosition> {
        val possibleMoves = mutableSetOf<BoardPosition>()
        val startRowIndex = piece.getCurrentPosition().rowIndex - 1
        val startColumnIndex = piece.getCurrentPosition().columnIndex + 1
        (0 until 8).forEachIndexed { step, shift ->
            if (step > piece.maxSteps()) {
                return possibleMoves
            }
            val nextValidRightUpPosition =
                BoardPosition(startRowIndex - shift, startColumnIndex + shift)
            if (!hasPieceOnPosition(nextValidRightUpPosition, boardPieces)) {
                possibleMoves.add(nextValidRightUpPosition)
            } else {
                return possibleMoves
            }
        }
        return possibleMoves
    }

    private fun getLeftDownPossibleMoves(
        piece: Piece,
        boardPieces: Set<Piece>
    ): Set<BoardPosition> {
        val possibleMoves = mutableSetOf<BoardPosition>()
        val startRowIndex = piece.getCurrentPosition().rowIndex + 1
        val startColumnIndex = piece.getCurrentPosition().columnIndex - 1
        (0 until 8).forEachIndexed { step, shift ->
            if (step > piece.maxSteps()) {
                return possibleMoves
            }
            val nextValidLeftDownPosition =
                BoardPosition(startRowIndex + shift, startColumnIndex - shift)
            if (!hasPieceOnPosition(nextValidLeftDownPosition, boardPieces)) {
                possibleMoves.add(nextValidLeftDownPosition)
            } else {
                return possibleMoves
            }
        }
        return possibleMoves
    }

    private fun getRightDownPossibleMoves(
        piece: Piece,
        boardPieces: Set<Piece>
    ): Set<BoardPosition> {
        val possibleMoves = mutableSetOf<BoardPosition>()
        val startRowIndex = piece.getCurrentPosition().rowIndex + 1
        val startColumnIndex = piece.getCurrentPosition().columnIndex + 1
        (0 until 8).forEachIndexed { step, shift ->
            if (step > piece.maxSteps()) {
                return possibleMoves
            }
            val nextValidRightDownPosition =
                BoardPosition(startRowIndex + shift, startColumnIndex + shift)
            if (!hasPieceOnPosition(nextValidRightDownPosition, boardPieces)) {
                possibleMoves.add(nextValidRightDownPosition)
            } else {
                return possibleMoves
            }
        }
        return possibleMoves
    }

}
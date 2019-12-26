package com.heshten.chess.core.logic.takecheckers

import com.heshten.chess.core.board.ChessBoard
import com.heshten.chess.core.logic.TakeChecker
import com.heshten.chess.core.models.BoardPosition
import com.heshten.chess.core.models.pieces.Piece

class VerticalTakeChecker(private val chessBoard: ChessBoard) : TakeChecker {

    override fun getPossibleTakes(piece: Piece): Set<BoardPosition> {
        return getPossibleVerticalTakes(piece)
    }

    private fun getPossibleVerticalTakes(piece: Piece): Set<BoardPosition> {
        val possibleTakes = mutableSetOf<BoardPosition>()
        val possibleUpTakes = getPossibleVerticalUpTakes(piece)
        val possibleDownTakes = getPossibleVerticalDownTakes(piece)
        possibleTakes.addAll(possibleUpTakes)
        possibleTakes.addAll(possibleDownTakes)
        return possibleTakes
    }

    private fun getPossibleVerticalUpTakes(piece: Piece): Set<BoardPosition> {
        val possibleMoves = mutableSetOf<BoardPosition>()
        val startRowPosition = piece.getCurrentPosition().rowIndex - 1
        val columnIndex = piece.getCurrentPosition().columnIndex
        (startRowPosition downTo 0).forEachIndexed { step, rowIndex ->
            if (step >= piece.maxSteps()) {
                return possibleMoves
            }
            val nextVerticalPosition = BoardPosition(rowIndex, columnIndex)
            val nextPiece = chessBoard.getPieceForPosition(nextVerticalPosition)
            if (nextPiece == null) {
                //move on
            } else {
                if (nextPiece.isOpposite(piece)) {
                    possibleMoves.add(nextVerticalPosition)
                }
                return possibleMoves
            }
        }
        return possibleMoves
    }

    private fun getPossibleVerticalDownTakes(piece: Piece): Set<BoardPosition> {
        val possibleTakes = mutableSetOf<BoardPosition>()
        val startRowPosition = piece.getCurrentPosition().rowIndex + 1
        val columnIndex = piece.getCurrentPosition().columnIndex
        (startRowPosition until 8).forEachIndexed { step, rowIndex ->
            if (step >= piece.maxSteps()) {
                return possibleTakes
            }
            val nextVerticalPosition = BoardPosition(rowIndex, columnIndex)
            val nextPiece = chessBoard.getPieceForPosition(nextVerticalPosition)
            if (nextPiece == null) {
                //move on
            } else {
                if (nextPiece.isOpposite(piece)) {
                    possibleTakes.add(nextVerticalPosition)
                }
                return possibleTakes
            }
        }
        return possibleTakes
    }

}
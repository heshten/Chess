package com.heshten.chess.core.logic.takecheckers

import com.heshten.chess.core.board.ChessBoard
import com.heshten.chess.core.logic.TakeChecker
import com.heshten.chess.core.models.BoardPosition
import com.heshten.chess.core.models.pieces.Piece

class KnightLikeTakeChecker(private val chessBoard: ChessBoard) : TakeChecker {

    override fun getPossibleTakes(piece: Piece): Set<BoardPosition> {
        return getPossibleKnightLikeTakes(piece)
    }

    private fun getPossibleKnightLikeTakes(piece: Piece): Set<BoardPosition> {
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
        maybeAddPossiblePosition(piece, upLeftPosition, possiblePositions)
        maybeAddPossiblePosition(piece, upRightPosition, possiblePositions)
        maybeAddPossiblePosition(piece, leftUpPosition, possiblePositions)
        maybeAddPossiblePosition(piece, leftDownPosition, possiblePositions)
        maybeAddPossiblePosition(piece, rightUpPosition, possiblePositions)
        maybeAddPossiblePosition(piece, rightDownPosition, possiblePositions)
        maybeAddPossiblePosition(piece, bottomLeftPosition, possiblePositions)
        maybeAddPossiblePosition(piece, bottomRightPosition, possiblePositions)
        return possiblePositions
    }

    private fun maybeAddPossiblePosition(
        piece: Piece,
        possiblePosition: BoardPosition,
        possibleTakesContainer: MutableSet<BoardPosition>
    ) {
        val nextPiece = chessBoard.getPieceForPosition(possiblePosition)
        if (nextPiece != null && nextPiece.isOpposite(piece)) {
            possibleTakesContainer.add(possiblePosition)
        }
    }
}
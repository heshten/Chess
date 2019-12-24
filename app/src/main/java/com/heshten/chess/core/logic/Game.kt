package com.heshten.chess.core.logic

import com.heshten.chess.core.logic.movecheckers.MoveChecker
import com.heshten.chess.core.models.BoardPosition
import com.heshten.chess.core.models.pieces.Piece
import com.heshten.chess.ui.views.IBoardView

class Game(
    private val board: Board,
    private val verticalMoveChecker: MoveChecker,
    private val horizontalMoveChecker: MoveChecker,
    private val diagonalMoveChecker: MoveChecker,
    private val knightLikeMoveChecker: MoveChecker,
    private val boardView: IBoardView
) {

    fun start() {
        redrawBoard()
    }

    fun pieceSelected(piece: Piece) {
        board.selectPiece(piece)
        board.setSelectedPositions(getPossibleMovesForPiece(piece))
        redrawBoard()
    }

    fun selectedPositionClicked(boardPosition: BoardPosition) {
        if (board.hasPieceAtPosition(boardPosition)) {
            //take piece
        } else {
            //perform move
            board.moveSelectedPieceToPosition(boardPosition)
            board.clearSelectedPositions()
            redrawBoard()
        }
    }

    private fun redrawBoard() {
        boardView.setPieces(board.pieces)
        boardView.setSelectedPositions(board.selectedPositions)
    }

    private fun getPossibleMovesForPiece(piece: Piece): Set<BoardPosition> {
        val possibleMoves = mutableSetOf<BoardPosition>()
        val possibleVerticalMoves = verticalMoveChecker.getPossibleMoves(piece)
        val possibleHorizontalMoves = horizontalMoveChecker.getPossibleMoves(piece)
        val possibleDiagonalMoves = diagonalMoveChecker.getPossibleMoves(piece)
        val possibleKnightLikeMoves = knightLikeMoveChecker.getPossibleMoves(piece)
        //merge
        possibleMoves.addAll(possibleVerticalMoves)
        possibleMoves.addAll(possibleHorizontalMoves)
        possibleMoves.addAll(possibleDiagonalMoves)
        possibleMoves.addAll(possibleKnightLikeMoves)
        return possibleMoves
    }

}
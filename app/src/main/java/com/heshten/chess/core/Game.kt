package com.heshten.chess.core

import com.heshten.chess.core.logic.MoveChecker
import com.heshten.chess.core.logic.TakeChecker
import com.heshten.chess.core.models.BoardPosition
import com.heshten.chess.core.models.pieces.Piece
import com.heshten.chess.ui.views.IBoardView

class Game(
    private val chessBoard: ChessBoard,
    private val boardView: IBoardView,
    private val movesCheckerFacade: MoveChecker,
    private val takesCheckerFacade: TakeChecker
) {

    fun start() {
        redrawBoard()
    }

    fun pieceSelected(piece: Piece) {
        chessBoard.selectPiece(piece)
        chessBoard.setSelectedPositions(getPossibleMovesForPiece(piece))
        redrawBoard()
    }

    fun selectedPositionClicked(boardPosition: BoardPosition) {
        if (chessBoard.hasPieceAtPosition(boardPosition)) {
            //take piece
        } else {
            //perform move
            chessBoard.moveSelectedPieceToPosition(boardPosition)
            chessBoard.clearSelectedPositions()
            redrawBoard()
        }
    }

    private fun redrawBoard() {
        boardView.setPieces(chessBoard.pieces)
        boardView.setSelectedPositions(chessBoard.selectedPositions)
    }

    private fun getPossibleMovesForPiece(piece: Piece): Set<BoardPosition> {
        val possibleMoves = mutableSetOf<BoardPosition>()
        possibleMoves.addAll(movesCheckerFacade.getPossibleMoves(piece))
        possibleMoves.addAll(takesCheckerFacade.getPossibleTakes(piece))
        return possibleMoves
    }

}
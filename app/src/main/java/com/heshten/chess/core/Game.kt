package com.heshten.chess.core

import com.heshten.chess.core.board.ChessBoard
import com.heshten.chess.core.logic.MoveChecker
import com.heshten.chess.core.logic.TakeChecker
import com.heshten.chess.core.models.BoardPosition
import com.heshten.chess.core.models.pieces.Piece
import com.heshten.chess.core.validator.SideMoveValidator
import com.heshten.chess.ui.views.IBoardView

class Game(
    private val chessBoard: ChessBoard,
    private val boardView: IBoardView,
    private val movesCheckerFacade: MoveChecker,
    private val takesCheckerFacade: TakeChecker,
    private val sideMoveValidator: SideMoveValidator
) {

    fun start() {
        redrawBoard()
    }

    fun pieceSelected(piece: Piece) {
        if (sideMoveValidator.getCurrentSide() == piece.pieceSide) {
            chessBoard.selectPiece(piece)
            chessBoard.setSelectedPositions(getPossibleMovesForPiece(piece))
            redrawBoard()
        }
    }

    fun selectedPositionClicked(boardPosition: BoardPosition) {
        if (chessBoard.hasPieceAtPosition(boardPosition)) {
            //take piece
            chessBoard.removePieceAtPosition(boardPosition)
            chessBoard.moveSelectedPieceToPosition(boardPosition)
            chessBoard.clearSelectedPositions()
        } else {
            //perform move
            chessBoard.moveSelectedPieceToPosition(boardPosition)
            chessBoard.clearSelectedPositions()
        }
        sideMoveValidator.changeSide()
        redrawBoard()
    }

    private fun redrawBoard() {
        boardView.redrawChessBoard(chessBoard)
    }

    private fun getPossibleMovesForPiece(piece: Piece): Set<BoardPosition> {
        val possibleMoves = mutableSetOf<BoardPosition>()
        possibleMoves.addAll(movesCheckerFacade.getPossibleMoves(piece))
        possibleMoves.addAll(takesCheckerFacade.getPossibleTakes(piece))
        return possibleMoves
    }

}
package com.heshten.chess.core.logic

import com.heshten.chess.core.logic.movecheckers.MoveChecker
import com.heshten.chess.core.models.BoardPosition
import com.heshten.chess.core.models.helpers.MoveDirection
import com.heshten.chess.core.models.pieces.*
import com.heshten.chess.core.recources.PieceResourceProvider
import com.heshten.chess.ui.views.IBoardView

class Game(
    private val blackPiecesResourceProvider: PieceResourceProvider,
    private val whitePiecesResourceProvider: PieceResourceProvider,
    private val verticalMoveChecker: MoveChecker,
    private val horizontalMoveChecker: MoveChecker,
    private val diagonalMoveChecker: MoveChecker,
    private val knightLikeMoveChecker: MoveChecker,
    private val boardView: IBoardView
) {


    private var selectedPositions = setOf<BoardPosition>()
    private var pieces = setOf<Piece>()
    private var selectedPiece: Piece? = null

    fun start() {
        pieces = createNewBoardPositions()
        selectedPositions = emptySet()
        invalidateBoard()
    }

    fun pieceSelected(piece: Piece) {
        selectedPiece = piece
        selectedPositions = getPossibleMovesForPiece(piece)
        invalidateBoard()
    }

    fun selectedPositionClicked(boardPosition: BoardPosition) {
        if (pieces.find { it.getCurrentPosition() == boardPosition } != null) {
            //take piece
        } else {
            //perform move
            selectedPiece?.moveTo(boardPosition)
            selectedPositions = emptySet()
            invalidateBoard()
        }
    }

    private fun invalidateBoard() {
        boardView.setPieces(pieces)
        boardView.setSelectedPositions(selectedPositions)
    }

    private fun getPossibleMovesForPiece(piece: Piece): Set<BoardPosition> {
        val possibleMoves = mutableSetOf<BoardPosition>()
        val possibleVerticalMoves = verticalMoveChecker.getPossibleMoves(piece, pieces)
        val possibleHorizontalMoves = horizontalMoveChecker.getPossibleMoves(piece, pieces)
        val possibleDiagonalMoves = diagonalMoveChecker.getPossibleMoves(piece, pieces)
        val possibleKnightLikeMoves = knightLikeMoveChecker.getPossibleMoves(piece, pieces)
        //merge
        possibleMoves.addAll(possibleVerticalMoves)
        possibleMoves.addAll(possibleHorizontalMoves)
        possibleMoves.addAll(possibleDiagonalMoves)
        possibleMoves.addAll(possibleKnightLikeMoves)
        return possibleMoves
    }

    private fun createNewBoardPositions() = setOf(
        //white pieces
        Rook(whitePiecesResourceProvider.getRookBitmap(), BoardPosition(0, 0)),
        Rook(whitePiecesResourceProvider.getRookBitmap(), BoardPosition(0, 7)),
        Knight(whitePiecesResourceProvider.getKnightBitmap(), BoardPosition(0, 1)),
        Knight(whitePiecesResourceProvider.getKnightBitmap(), BoardPosition(0, 6)),
        Bishop(whitePiecesResourceProvider.getBishopBitmap(), BoardPosition(0, 2)),
        Bishop(whitePiecesResourceProvider.getBishopBitmap(), BoardPosition(0, 5)),
        King(whitePiecesResourceProvider.getKingBitmap(), BoardPosition(0, 3)),
        Queen(whitePiecesResourceProvider.getQueenBitmap(), BoardPosition(0, 4)),

        Pawn(whitePiecesResourceProvider.getPawnBitmap(), BoardPosition(1, 0), MoveDirection.DOWN),
        Pawn(whitePiecesResourceProvider.getPawnBitmap(), BoardPosition(1, 1), MoveDirection.DOWN),
        Pawn(whitePiecesResourceProvider.getPawnBitmap(), BoardPosition(1, 2), MoveDirection.DOWN),
        Pawn(whitePiecesResourceProvider.getPawnBitmap(), BoardPosition(1, 3), MoveDirection.DOWN),
        Pawn(whitePiecesResourceProvider.getPawnBitmap(), BoardPosition(1, 4), MoveDirection.DOWN),
        Pawn(whitePiecesResourceProvider.getPawnBitmap(), BoardPosition(1, 5), MoveDirection.DOWN),
        Pawn(whitePiecesResourceProvider.getPawnBitmap(), BoardPosition(1, 6), MoveDirection.DOWN),
        Pawn(whitePiecesResourceProvider.getPawnBitmap(), BoardPosition(1, 7), MoveDirection.DOWN),

        //black pieces
        Pawn(blackPiecesResourceProvider.getPawnBitmap(), BoardPosition(6, 0), MoveDirection.UP),
        Pawn(blackPiecesResourceProvider.getPawnBitmap(), BoardPosition(6, 1), MoveDirection.UP),
        Pawn(blackPiecesResourceProvider.getPawnBitmap(), BoardPosition(6, 2), MoveDirection.UP),
        Pawn(blackPiecesResourceProvider.getPawnBitmap(), BoardPosition(6, 3), MoveDirection.UP),
        Pawn(blackPiecesResourceProvider.getPawnBitmap(), BoardPosition(6, 4), MoveDirection.UP),
        Pawn(blackPiecesResourceProvider.getPawnBitmap(), BoardPosition(6, 5), MoveDirection.UP),
        Pawn(blackPiecesResourceProvider.getPawnBitmap(), BoardPosition(6, 6), MoveDirection.UP),
        Pawn(blackPiecesResourceProvider.getPawnBitmap(), BoardPosition(6, 7), MoveDirection.UP),

        Rook(blackPiecesResourceProvider.getRookBitmap(), BoardPosition(7, 0)),
        Rook(blackPiecesResourceProvider.getRookBitmap(), BoardPosition(7, 7)),
        Knight(blackPiecesResourceProvider.getKnightBitmap(), BoardPosition(7, 1)),
        Knight(blackPiecesResourceProvider.getKnightBitmap(), BoardPosition(7, 6)),
        Bishop(blackPiecesResourceProvider.getBishopBitmap(), BoardPosition(7, 2)),
        Bishop(blackPiecesResourceProvider.getBishopBitmap(), BoardPosition(7, 5)),
        King(blackPiecesResourceProvider.getKingBitmap(), BoardPosition(7, 3)),
        Queen(blackPiecesResourceProvider.getQueenBitmap(), BoardPosition(7, 4))
    )

}
package com.heshten.core.logic

import com.heshten.core.board.ChessBoard
import com.heshten.core.models.BoardPosition
import com.heshten.core.models.PieceSide
import com.heshten.core.models.pieces.King
import com.heshten.core.models.pieces.Piece

class PossibleMovesCalculator(
  private val movesCheckerFacade: MoveChecker,
  private val takesCheckerFacade: TakeChecker
) {

  fun calculatePossibleMovesForPiece(
    piece: Piece,
    chessBoard: ChessBoard
  ): Set<BoardPosition> {
    val possibleMoves = getAllPossibleMovesAndTakes(piece, chessBoard)
    excludeMovesThatLeadsToCheckInPlace(piece, chessBoard, possibleMoves)
    return possibleMoves
  }

  fun isCheck(chessBoard: ChessBoard, side: PieceSide): Boolean {
    val allPieces = chessBoard.getAllPieces()
    val king = allPieces.first { it.pieceSide == side && it is King }
    allPieces.filter { it.pieceSide != side }.forEach { piece ->
      val possibleMoves = getAllPossibleMovesAndTakes(piece, chessBoard)
      if (possibleMoves.contains(king.boardPosition)) {
        return true
      }
    }
    return false
  }

  private fun excludeMovesThatLeadsToCheckInPlace(
    piece: Piece,
    chessBoard: ChessBoard,
    mutableSet: MutableSet<BoardPosition>
  ) {
    mutableSet.toSet().forEach { boardPosition ->
      val originPieces = chessBoard.getAllPieces().toMutableSet()
      val chessBoardSnapshot = ChessBoard(originPieces)
      chessBoardSnapshot.selectPiece(piece)
      chessBoardSnapshot.moveSelectedPieceToPosition(boardPosition)
      if (isCheck(chessBoardSnapshot, piece.pieceSide)) {
        mutableSet.remove(boardPosition)
      }
    }
  }

  private fun getAllPossibleMovesAndTakes(
    piece: Piece,
    chessBoard: ChessBoard
  ): MutableSet<BoardPosition> {
    val possibleMoves = mutableSetOf<BoardPosition>()
    possibleMoves.addAll(movesCheckerFacade.getPossibleMoves(piece, chessBoard))
    possibleMoves.addAll(takesCheckerFacade.getPossibleTakes(piece, chessBoard))
    return possibleMoves
  }
}

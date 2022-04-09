package com.heshten.core.logic

import com.heshten.core.board.ChessBoard
import com.heshten.core.models.BoardPosition
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
    val possibleMoves = mutableSetOf<BoardPosition>()
    possibleMoves.addAll(movesCheckerFacade.getPossibleMoves(piece, chessBoard))
    possibleMoves.addAll(takesCheckerFacade.getPossibleTakes(piece, chessBoard))
    excludeMovesThatLeadsToCheckInPlace(piece, chessBoard, possibleMoves)
    return possibleMoves
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

      val kingPiece = chessBoardSnapshot.getAllPieces()
        .first { it is King && it.pieceSide == piece.pieceSide }

      chessBoardSnapshot.getAllPieces()
        .filter { it.pieceSide != piece.pieceSide }
        .forEach { snapshotPiece ->
          val possibleMoves = mutableSetOf<BoardPosition>()

          possibleMoves.addAll(
            movesCheckerFacade.getPossibleMoves(snapshotPiece, chessBoardSnapshot)
          )
          possibleMoves.addAll(
            takesCheckerFacade.getPossibleTakes(snapshotPiece, chessBoardSnapshot)
          )

          if (possibleMoves.contains(kingPiece.boardPosition)) {
            mutableSet.remove(boardPosition)
          }
        }
    }
  }
}

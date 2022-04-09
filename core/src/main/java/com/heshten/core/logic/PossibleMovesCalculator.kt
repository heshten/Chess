package com.heshten.core.logic

import com.heshten.core.board.ChessBoard
import com.heshten.core.models.BoardPosition
import com.heshten.core.models.pieces.King
import com.heshten.core.models.pieces.Piece
import com.heshten.core.models.pieces.Rook

class PossibleMovesCalculator(
  private val movesCheckerFacade: MoveChecker,
  private val takesCheckerFacade: TakeChecker
) {

  fun calculatePossibleMovesForPiece(
    piece: Piece,
    chessBoard: ChessBoard
  ): Set<BoardPosition> {
    val possibleMoves = getAllPossibleMovesAndTakes(piece, chessBoard)
    addCastlingPositionsInPlace(piece, chessBoard, possibleMoves)
    excludeMovesThatLeadsToCheckInPlace(piece, chessBoard, possibleMoves)
    return possibleMoves
  }

  fun isUnderAttack(piece: Piece, chessBoard: ChessBoard): Boolean {
    val allPieces = chessBoard.getAllPieces()
    allPieces.filter { it.pieceSide != piece.pieceSide }.forEach { iterationPiece ->
      val possibleMoves = getAllPossibleMovesAndTakes(iterationPiece, chessBoard)
      if (possibleMoves.contains(piece.boardPosition)) {
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
      val king = chessBoardSnapshot.getAllPieces()
        .first { it is King && it.pieceSide == piece.pieceSide }
      if (isUnderAttack(king, chessBoardSnapshot)) {
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

  /**
   * Castling can be performed if all of the statements are true:
   * 1) Castling Rook and King has never been moved;
   * 2) No piece between Castling Rook and King;
   * 3) King is not under attack;
   * 4) Castling Root is not be under attack after castling;
   * 5) King is not under check after castling;
   */
  private fun addCastlingPositionsInPlace(
    piece: Piece,
    chessBoard: ChessBoard,
    mutableSet: MutableSet<BoardPosition>
  ) {
    if (piece is King && !piece.firstMovePerformed) {
      addShortCastlingPositionsInPlace(piece, chessBoard, mutableSet)
      addLongCastlingPositionsInPlace(piece, chessBoard, mutableSet)
    }
  }

  private fun addShortCastlingPositionsInPlace(
    piece: Piece,
    chessBoard: ChessBoard,
    mutableSet: MutableSet<BoardPosition>
  ) {
    val row = piece.boardPosition.rowIndex
    val rightRookPosition = BoardPosition(row, 7)
    val pieceAtRightRookPosition = chessBoard.getPieceAtPosition(rightRookPosition)
    if (pieceAtRightRookPosition is Rook && !pieceAtRightRookPosition.firstMovePerformed) {
      // check no piece in between;
      if (
        !chessBoard.hasPieceAtPosition(BoardPosition(row, 6)) &&
        !chessBoard.hasPieceAtPosition(BoardPosition(row, 5))
      ) {
        // check King is not under attack
        if (!isUnderAttack(piece, chessBoard)) {
          // try perform castling
          val chessboardSnapshot = ChessBoard(chessBoard.getAllPieces())
          val king = chessboardSnapshot.getPieceAtPosition(BoardPosition(row, 4))!!
          val rook = chessboardSnapshot.getPieceAtPosition(BoardPosition(row, 7))!!
          chessboardSnapshot.selectPiece(king)
          chessboardSnapshot.moveSelectedPieceToPosition(BoardPosition(row, 6))
          chessboardSnapshot.selectPiece(rook)
          chessboardSnapshot.moveSelectedPieceToPosition(BoardPosition(row, 5))
          val kingAfter = chessboardSnapshot.getPieceAtPosition(BoardPosition(row, 6))!!
          val rookAfter = chessboardSnapshot.getPieceAtPosition(BoardPosition(row, 5))!!
          if (
            !isUnderAttack(kingAfter, chessboardSnapshot) &&
            !isUnderAttack(rookAfter, chessBoard)
          ) {
            // possible for castling so add the positions
            mutableSet.add(BoardPosition(row, 6))
          }
        }
      }
    }
  }

  private fun addLongCastlingPositionsInPlace(
    piece: Piece,
    chessBoard: ChessBoard,
    mutableSet: MutableSet<BoardPosition>
  ) {
    val row = piece.boardPosition.rowIndex
    val leftRookPosition = BoardPosition(row, 0)
    val pieceAtLeftRookPosition = chessBoard.getPieceAtPosition(leftRookPosition)
    if (pieceAtLeftRookPosition is Rook && !pieceAtLeftRookPosition.firstMovePerformed) {
      // check no piece in between;
      if (
        !chessBoard.hasPieceAtPosition(BoardPosition(row, 1)) &&
        !chessBoard.hasPieceAtPosition(BoardPosition(row, 2)) &&
        !chessBoard.hasPieceAtPosition(BoardPosition(row, 3))
      ) {
        // check King is not under attack
        if (!isUnderAttack(piece, chessBoard)) {
          // try perform castling
          val chessboardSnapshot = ChessBoard(chessBoard.getAllPieces())
          val king = chessboardSnapshot.getPieceAtPosition(BoardPosition(row, 4))!!
          val rook = chessboardSnapshot.getPieceAtPosition(BoardPosition(row, 0))!!
          chessboardSnapshot.selectPiece(king)
          chessboardSnapshot.moveSelectedPieceToPosition(BoardPosition(row, 2))
          chessboardSnapshot.selectPiece(rook)
          chessboardSnapshot.moveSelectedPieceToPosition(BoardPosition(row, 3))
          val kingAfter = chessboardSnapshot.getPieceAtPosition(BoardPosition(row, 2))!!
          val rookAfter = chessboardSnapshot.getPieceAtPosition(BoardPosition(row, 3))!!
          if (
            !isUnderAttack(kingAfter, chessboardSnapshot) &&
            !isUnderAttack(rookAfter, chessBoard)
          ) {
            // possible for castling so add the positions
            mutableSet.add(BoardPosition(row, 2))
          }
        }
      }
    }
  }
}

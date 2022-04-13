package com.heshten.core.board

import com.heshten.core.logic.MoveChecker
import com.heshten.core.logic.TakeChecker
import com.heshten.core.models.BoardPosition
import com.heshten.core.models.Move
import com.heshten.core.models.opposite
import com.heshten.core.models.pieces.King
import com.heshten.core.models.pieces.Piece
import com.heshten.core.models.pieces.Rook


class PossibleMovesCalculator(
  private val movesCheckerFacade: MoveChecker,
  private val takesCheckerFacade: TakeChecker
) {

  fun calculatePossibleMovesForPiece(piece: Piece, board: ChessBoard): Set<Move> {
    // must be a copy of board to avoid concurrent exceptions.
    val chessBoardCopy = board.copy()
    val possibleMoves = getAllPossibleMovesAndTakes(piece, chessBoardCopy)
      .map { Move.Regular(piece, piece.boardPosition, it) }
      .toMutableSet<Move>()
    addCastlingPositionsInPlace(piece, chessBoardCopy, possibleMoves)
    excludeMovesThatLeadsToCheckInPlace(piece, chessBoardCopy, possibleMoves)
    return possibleMoves
  }

  fun isUnderAttack(piece: Piece, board: ChessBoard): Boolean {
    board.getAllPiecesForSide(piece.pieceSide.opposite()).forEach { iterationPiece ->
      val possibleMoves = getAllPossibleMovesAndTakes(iterationPiece, board)
      if (possibleMoves.contains(piece.boardPosition)) {
        return true
      }
    }
    return false
  }

  private fun excludeMovesThatLeadsToCheckInPlace(
    piece: Piece,
    chessBoard: ChessBoard,
    mutableSet: MutableSet<Move>
  ) {
    mutableSet.toSet().forEach { move ->
      chessBoard.doMove(move)
      if (chessBoard.isCheck(piece.pieceSide)) {
        mutableSet.remove(move)
      }
      chessBoard.undo()
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
    mutableSet: MutableSet<Move>
  ) {
    if (piece is King && !piece.firstMovePerformed) {
      addShortCastlingPositionsInPlace(piece, chessBoard, mutableSet)
      addLongCastlingPositionsInPlace(piece, chessBoard, mutableSet)
    }
  }

  private fun addShortCastlingPositionsInPlace(
    king: King,
    chessBoard: ChessBoard,
    mutableSet: MutableSet<Move>
  ) {
    val row = king.boardPosition.rowIndex
    val rightRookPosition = BoardPosition(row, 7)
    val rook = chessBoard.getPieceAtPosition(rightRookPosition)
    if (rook is Rook && !rook.firstMovePerformed) {
      // check no piece in between;
      if (
        !chessBoard.hasPieceAtPosition(BoardPosition(row, 6)) &&
        !chessBoard.hasPieceAtPosition(BoardPosition(row, 5))
      ) {
        // check King is not under attack
        if (!isUnderAttack(king, chessBoard)) {
          val kingMovePosition = BoardPosition(row, 6)
          val rookMovePosition = BoardPosition(row, 5)
          val castling = Move.Castling(
            Move.Regular(king, king.boardPosition, kingMovePosition),
            Move.Regular(rook, rook.boardPosition, rookMovePosition)
          )
          chessBoard.doMove(castling)
          val kingAfter = chessBoard.getPieceAtPosition(kingMovePosition)!!
          val rookAfter = chessBoard.getPieceAtPosition(rookMovePosition)!!
          if (
            !isUnderAttack(kingAfter, chessBoard) &&
            !isUnderAttack(rookAfter, chessBoard)
          ) {
            // possible for castling so add the move.
            mutableSet.add(castling)
          }
          chessBoard.undo()
        }
      }
    }
  }

  private fun addLongCastlingPositionsInPlace(
    king: King,
    chessBoard: ChessBoard,
    mutableSet: MutableSet<Move>
  ) {
    val row = king.boardPosition.rowIndex
    val leftRookPosition = BoardPosition(row, 0)
    val rook = chessBoard.getPieceAtPosition(leftRookPosition)
    if (rook is Rook && !rook.firstMovePerformed) {
      // check no piece in between;
      if (
        !chessBoard.hasPieceAtPosition(BoardPosition(row, 1)) &&
        !chessBoard.hasPieceAtPosition(BoardPosition(row, 2)) &&
        !chessBoard.hasPieceAtPosition(BoardPosition(row, 3))
      ) {
        // check King is not under attack
        if (!isUnderAttack(king, chessBoard)) {
          val kingMovePosition = BoardPosition(row, 2)
          val rookMovePosition = BoardPosition(row, 3)
          val castling = Move.Castling(
            Move.Regular(king, king.boardPosition, kingMovePosition),
            Move.Regular(rook, rook.boardPosition, rookMovePosition)
          )
          chessBoard.doMove(castling)
          val kingAfter = chessBoard.getPieceAtPosition(kingMovePosition)!!
          val rookAfter = chessBoard.getPieceAtPosition(rookMovePosition)!!
          if (
            !isUnderAttack(kingAfter, chessBoard) &&
            !isUnderAttack(rookAfter, chessBoard)
          ) {
            // possible for castling so add the move.
            mutableSet.add(castling)
          }
          chessBoard.undo()
        }
      }
    }
  }
}
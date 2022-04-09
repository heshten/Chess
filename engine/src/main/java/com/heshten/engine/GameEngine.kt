package com.heshten.engine

import com.heshten.core.board.ChessBoard
import com.heshten.core.logic.PossibleMovesCalculator
import com.heshten.core.models.BoardPosition
import com.heshten.core.models.PieceSide
import com.heshten.core.models.opposite
import com.heshten.core.models.pieces.*

class GameEngine(
  val engineSide: PieceSide,
  private val chessBoard: ChessBoard,
  private val possibleMovesCalculator: PossibleMovesCalculator
) {

  fun calculateNextMove(): Move {
    val rankMap = mutableMapOf<Int, Move>()
    chessBoard.getAllPieces().forEach { piece ->
      if (piece.pieceSide == engineSide) {
        val possibleMoves = possibleMovesCalculator
          .calculatePossibleMovesForPiece(piece, chessBoard)
        possibleMoves.forEach { possibleMovePosition ->

          val allPiecesMutableSet = chessBoard.getAllPieces().toMutableSet()
          val chessboardSnapshot = ChessBoard(allPiecesMutableSet)
          chessboardSnapshot.selectPiece(piece)
          chessboardSnapshot.moveSelectedPieceToPosition(possibleMovePosition)

          rankMap[calculateBoardRank(chessboardSnapshot, engineSide)] =
            Move(piece.boardPosition, possibleMovePosition)
        }
      }
    }

    var topRankMove = rankMap.entries.first()
    rankMap.entries.forEach { entry ->
      if (entry.key > topRankMove.key) topRankMove = entry
    }
    // simulate "thinking"
    Thread.sleep(200)
    return topRankMove.value
  }

  private fun calculateBoardRank(chessBoard: ChessBoard, side: PieceSide): Int {
    // find all possible attacks
    val currentPiecesUnderAttack = calculatePiecesUnderAttack(chessBoard, side)
    val oppositePiecesUnderAttack = calculatePiecesUnderAttack(chessBoard, side.opposite())
    var rank = 0
    currentPiecesUnderAttack.forEach { piece -> rank -= getPieceRank(piece) }
    oppositePiecesUnderAttack.forEach { piece -> rank += getPieceRank(piece) }
    return rank
  }

  private fun calculatePiecesUnderAttack(chessBoard: ChessBoard, side: PieceSide): Set<Piece> {
    val pieces = chessBoard.getAllPieces().filter { it.pieceSide == side }.toSet()
    val piecesUnderAttack = mutableSetOf<Piece>()
    pieces.forEach { piece ->
      val possibleMoves = possibleMovesCalculator
        .calculatePossibleMovesForPiece(piece, chessBoard)
      possibleMoves.forEach { movePosition ->
        val pieceAtPosition = pieces
          .find { it.boardPosition == movePosition }
        if (pieceAtPosition != null) {
          piecesUnderAttack.add(pieceAtPosition)
        }
      }
    }
    return piecesUnderAttack
  }

  private fun getPieceRank(piece: Piece): Int {
    return when (piece) {
      is Bishop -> 25
      is King -> 100
      is Knight -> 15
      is Pawn -> 5
      is Queen -> 50
      is Rook -> 35
      else -> throw IllegalArgumentException()
    }
  }

  data class Move(
    val fromPosition: BoardPosition,
    val toPosition: BoardPosition
  )
}

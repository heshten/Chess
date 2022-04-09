package com.heshten.engine

import com.heshten.core.Game
import com.heshten.core.board.ChessBoard
import com.heshten.core.models.BoardPosition
import com.heshten.core.models.PieceSide
import java.util.*
import kotlin.random.Random

class GameEngine(
  private val game: Game,
  private val chessBoard: ChessBoard,
  val engineSide: PieceSide
) {

  fun calculateNextMove(): Move {
    val rankMap = mutableMapOf<Int, Move>()
    chessBoard.getAllPieces().forEach { piece ->
      if (piece.pieceSide == engineSide) {
        val possibleMoves = game.calculatePossibleMovesForPiece(piece)
        possibleMoves.forEach { possibleMovePosition ->
          val allPiecesMutableSet = chessBoard.getAllPieces().toMutableSet()
          allPiecesMutableSet.remove(piece)
          val pieceCopy = piece.copy()
          pieceCopy.moveTo(possibleMovePosition)
          allPiecesMutableSet.add(pieceCopy)
          val chessboardSnapshot = ChessBoard(allPiecesMutableSet)
          rankMap[calculateBoardRank(chessboardSnapshot, engineSide)] =
            Move(piece.getCurrentPosition(), possibleMovePosition)
        }
      }
    }
    var topRankMove = rankMap.entries.first()
    rankMap.entries.forEach { entry ->
      if (entry.key > topRankMove.key) topRankMove = entry
    }
    return topRankMove.value
  }

  private fun calculateBoardRank(chessBoard: ChessBoard, side: PieceSide): Int {
    // todo: Implement rank calculation for each chessboard snapshot
    return Random(UUID.randomUUID().hashCode()).nextInt(0, 1000)
  }

  data class Move(
    val fromPosition: BoardPosition,
    val toPosition: BoardPosition
  )
}

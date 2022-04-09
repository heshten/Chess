package com.heshten.engine

import com.heshten.core.board.ChessBoard
import com.heshten.core.logic.PossibleMovesCalculator
import com.heshten.core.models.BoardPosition
import com.heshten.core.models.PieceSide
import java.util.*
import kotlin.random.Random

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
    // todo: Implement rank calculation for each chessboard snapshot
    return Random(UUID.randomUUID().hashCode()).nextInt(0, 1000)
  }

  data class Move(
    val fromPosition: BoardPosition,
    val toPosition: BoardPosition
  )
}

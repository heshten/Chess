package com.heshten.engine

import com.heshten.core.Game
import com.heshten.core.board.ChessBoard
import com.heshten.core.models.BoardPosition
import com.heshten.core.models.PieceSide
import com.heshten.core.validator.SideMoveValidator
import java.util.*
import kotlin.random.Random

class GameEngine(
  private val game: Game,
  private val chessBoard: ChessBoard,
  private val engineSide: PieceSide,
  private val moveValidator: SideMoveValidator
) {

  fun performMove() {
    if (moveValidator.getCurrentSide() == engineSide) {
      val rankMap = mutableMapOf<Int, MoveSimulation>()
      chessBoard.getAllPieces().forEach { piece ->
        if (piece.pieceSide == engineSide) {
          val possibleMoves = game.getPossibleMovesForPiece(piece)
          possibleMoves.forEach { possibleMovePosition ->
            rankMap[calculateBoardRank(chessBoard, engineSide)] =
              MoveSimulation(piece.getCurrentPosition(), possibleMovePosition)
          }
        }
      }
      var topRankMove = rankMap.entries.first()
      rankMap.entries.forEach { entry ->
        if (entry.key > topRankMove.key) topRankMove = entry
      }
      // simulate move.
      game.onPositionTouched(topRankMove.value.fromPosition)
      game.onPositionTouched(topRankMove.value.toPosition)
    }
  }

  private fun calculateBoardRank(chessBoard: ChessBoard, side: PieceSide): Int {
    // todo: Implement
    val rank = Random(UUID.randomUUID().hashCode()).nextInt(0, 1000)
    System.out.println(rank)
    return rank
  }

  data class MoveSimulation(
    val fromPosition: BoardPosition,
    val toPosition: BoardPosition
  )
}

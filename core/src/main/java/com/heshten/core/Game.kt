package com.heshten.core

import com.heshten.core.board.ChessBoard
import com.heshten.core.models.Move
import com.heshten.core.models.PieceSide
import com.heshten.core.models.opposite
import com.heshten.core.models.pieces.Piece
import com.heshten.core.validator.SideMoveValidator

class Game(
  private val chessBoard: ChessBoard,
  private val sideMoveValidator: SideMoveValidator,
  private val redrawBoard: (Map<Piece, Set<Move>>) -> Unit,
  private val gameFinished: (GameResult) -> Unit
) {

  init {
    redrawBoardInternal()
  }

  fun doMove(move: Move) {
    chessBoard.doMove(move)
    val nextMoveSide = sideMoveValidator.getCurrentSide().opposite()
    val hasNextMoves = chessBoard.hasNextMoves(nextMoveSide)
    if (chessBoard.isCheck(nextMoveSide) && !hasNextMoves) {
      // check and mate
      gameFinished.invoke(GameResult.Winner(sideMoveValidator.getCurrentSide()))
      redrawBoardInternal()
    } else if (!hasNextMoves) {
      // draw
      gameFinished.invoke(GameResult.Draw)
      redrawBoardInternal()
    } else {
      sideMoveValidator.changeSide()
      redrawBoardInternal()
    }
  }

  private fun redrawBoardInternal() {
    val result = mutableMapOf<Piece, Set<Move>>()
    chessBoard.getAllPieces().forEach { piece ->
      result[piece] = if (piece.pieceSide == sideMoveValidator.getCurrentSide()) {
        chessBoard.getPossibleMovesForPiece(piece)
      } else {
        emptySet()
      }
    }
    redrawBoard.invoke(result)
  }

  sealed class GameResult {
    object Draw : GameResult()
    data class Winner(val side: PieceSide) : GameResult()
  }
}

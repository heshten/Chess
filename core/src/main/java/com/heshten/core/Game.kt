package com.heshten.core

import com.heshten.core.board.ChessBoard
import com.heshten.core.models.Move
import com.heshten.core.models.PieceSide
import com.heshten.core.models.opposite
import com.heshten.core.models.pieces.Piece
import com.heshten.core.validator.SideMoveValidator

class Game(
  private val playerSide: PieceSide,
  private val chessBoard: ChessBoard,
  private val sideMoveValidator: SideMoveValidator,
  private val performMoveForSide: (PieceSide) -> Unit,
  private val redrawBoard: (RedrawModel) -> Unit,
  private val gameFinished: (GameResult) -> Unit
) {

  init {
    redrawBoardInternal()
    performMoveForSide.invoke(sideMoveValidator.getCurrentSide())
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
      // next move is allowed
      performMoveForSide.invoke(sideMoveValidator.getCurrentSide())
    }
  }

  fun undo() {
    if (playerSide == sideMoveValidator.getCurrentSide()) {
      if (
        !chessBoard.hasNextMoves(playerSide) ||
        !chessBoard.hasNextMoves(playerSide.opposite())
      ) {
        // undo after game has finished is not allowed.
        return
      }
      chessBoard.undo()
      chessBoard.undo()
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
    redrawBoard.invoke(RedrawModel(result, sideMoveValidator.getCurrentSide()))
  }

  data class RedrawModel(
    val chessBoardData: Map<Piece, Set<Move>>,
    val currentMoveSide: PieceSide
  )

  sealed class GameResult {
    object Draw : GameResult()
    data class Winner(val side: PieceSide) : GameResult()
  }
}

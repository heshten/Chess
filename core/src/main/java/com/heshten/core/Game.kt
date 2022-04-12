package com.heshten.core

import com.heshten.core.board.ChessBoard
import com.heshten.core.logic.PossibleMovesCalculator
import com.heshten.core.models.BoardPosition
import com.heshten.core.models.Move
import com.heshten.core.models.PieceSide
import com.heshten.core.models.opposite
import com.heshten.core.models.pieces.King
import com.heshten.core.models.pieces.Piece
import com.heshten.core.validator.SideMoveValidator

class Game(
  private val chessBoard: ChessBoard,
  private val possibleMovesCalculator: PossibleMovesCalculator,
  private val sideMoveValidator: SideMoveValidator,
  private val redrawBoard: (Set<Piece>, Set<BoardPosition>) -> Unit,
  private val gameFinished: (GameResult) -> Unit
) {

  private var selectedPiece: Piece? = null

  init {
    redrawBoardInternal()
  }

  @Suppress("ControlFlowWithEmptyBody")
  fun onPositionTouched(boardPosition: BoardPosition) {
    val selectedPieceLocal = selectedPiece
    if (selectedPieceLocal != null) {
      val pieceAtPosition = chessBoard.getPieceAtPosition(boardPosition)
      if (pieceAtPosition == selectedPieceLocal) {
        // deselect the piece.
        selectedPiece = null
        redrawBoardInternal()
      } else {
        val possibleMoves = possibleMovesCalculator
          .calculatePossibleMovesForPiece(selectedPieceLocal, chessBoard)
        if (possibleMoves.contains(boardPosition)) {
          // it is possible to move there.
          move(Move(selectedPieceLocal, selectedPieceLocal.boardPosition, boardPosition))
          selectedPiece = null
          redrawBoardInternal()
        } else {
          // not a possible move position, ignore the action.
        }
      }
    } else {
      val pieceAtPosition = chessBoard.getPieceAtPosition(boardPosition)
      if (pieceAtPosition == null) {
        // empty square touched. do nothing.
      } else {
        if (pieceAtPosition.pieceSide == sideMoveValidator.getCurrentSide()) {
          // it is the turn for the given side to perform a move.
          selectedPiece = pieceAtPosition
          redrawBoardInternal()
        } else {
          // it is the turn to perform a move on the opposite side. do nothing.
        }
      }
    }
  }

  private fun move(move: Move) {
    chessBoard.doMove(move)
    val nextMoveSide = sideMoveValidator.getCurrentSide().opposite()
    val hasNextMoves = hasNextMoves(nextMoveSide)
    if (isCheck(nextMoveSide) && !hasNextMoves) {
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
    val selectedPieceLocal = selectedPiece
    val possibleMovePositions = mutableSetOf<BoardPosition>()
    if (selectedPieceLocal != null) {
      possibleMovePositions.addAll(
        possibleMovesCalculator.calculatePossibleMovesForPiece(selectedPieceLocal, chessBoard)
      )
    }
    redrawBoard.invoke(chessBoard.getAllPieces(), possibleMovePositions)
  }

  private fun isCheck(side: PieceSide): Boolean {
    val king = chessBoard.getAllPieces().first { it.pieceSide == side && it is King }
    return possibleMovesCalculator.isUnderAttack(king, chessBoard)
  }

  private fun hasNextMoves(side: PieceSide): Boolean {
    var possibleMoves = 0
    chessBoard.getAllPiecesForSide(side).forEach { piece ->
      possibleMoves += possibleMovesCalculator
        .calculatePossibleMovesForPiece(piece, chessBoard).size
    }
    return possibleMoves > 0
  }

  sealed class GameResult {
    object Draw : GameResult()
    data class Winner(val side: PieceSide) : GameResult()
  }
}

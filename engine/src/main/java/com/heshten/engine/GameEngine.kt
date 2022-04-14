package com.heshten.engine

import com.heshten.core.board.ChessBoard
import com.heshten.core.models.Move
import com.heshten.core.models.PieceSide
import com.heshten.core.models.opposite
import com.heshten.core.models.pieces.Piece

class GameEngine(
  val engineSide: PieceSide,
  private val maxSearchDepth: Int,
  private val chessBoard: ChessBoard
) {

  fun calculateNextMove(): Move {
    val chessBoardCopy = chessBoard.copy()
    dfsMove(chessBoardCopy, 0, maxSearchDepth)
    // dfs is still under development so return a random move.
    return randomMove(chessBoardCopy, engineSide)
  }

  private fun randomMove(chessBoard: ChessBoard, pieceSide: PieceSide): Move {
    val resultMap = mutableSetOf<Move>()
    chessBoard.getAllPiecesForSide(pieceSide).toSet().forEach { piece ->
      resultMap.addAll(chessBoard.getPossibleMovesForPiece(piece))
    }
    return resultMap.random()
  }

  private fun calculateBoardRank(chessBoard: ChessBoard, side: PieceSide): Int {
    val resultMap = mutableMapOf<Piece, PieceRank>()
    chessBoard.getAllPiecesForSide(side).forEach { piece ->
      resultMap[piece] = calculateRankForPiece(piece, chessBoard)
    }
    return resultMap.mapToBoardRank()
  }

  private fun dfsMove(chessBoard: ChessBoard, depth: Int, maxDepth: Int) {
    if (depth >= maxDepth || !chessBoard.hasNextMoves(engineSide)) {
      return
    }
    chessBoard.getAllPiecesForSide(engineSide).forEach { enginePiece ->
      val possibleEngineMoves = chessBoard.getPossibleMovesForPiece(enginePiece)
      possibleEngineMoves.forEach { possibleEngineMove ->
        chessBoard.doMove(possibleEngineMove)
        chessBoard.getAllPiecesForSide(engineSide.opposite()).forEach { oppositePiece ->
          val possibleOppositeMoves = chessBoard.getPossibleMovesForPiece(oppositePiece)
          possibleOppositeMoves.forEach { possibleOppositeMove ->
            chessBoard.doMove(possibleOppositeMove)
            dfsMove(chessBoard, depth + 1, maxSearchDepth)
            chessBoard.undo()
          }
        }
        chessBoard.undo()
      }
    }
  }

  private fun calculateRankForPiece(piece: Piece, chessBoard: ChessBoard): PieceRank {
    return PieceRank(
      calculateMobilityForPiece(piece, chessBoard).size,
      calculateAttacksForPiece(piece, chessBoard).size,
      calculateProtectsForPiece(piece, chessBoard).size
    )
  }

  private fun calculateMobilityForPiece(piece: Piece, chessBoard: ChessBoard): Set<Move> {
    val mobility = mutableSetOf<Move>()
    val possibleMoves = chessBoard.getPossibleMovesForPiece(piece)
    possibleMoves.forEach { move ->
      when (move) {
        is Move.Castling -> {
          // todo
        }
        is Move.Regular -> {
          if (!chessBoard.hasPieceAtPosition(move.toPosition)) {
            mobility.add(move)
          }
        }
      }
    }
    return mobility
  }

  private fun calculateAttacksForPiece(piece: Piece, chessBoard: ChessBoard): Set<Piece> {
    val attacks = mutableSetOf<Piece>()
    val possibleMoves = chessBoard.getPossibleMovesForPiece(piece)
    possibleMoves.forEach { move ->
      when (move) {
        is Move.Castling -> {
          // todo
        }
        is Move.Regular -> {
          if (chessBoard.hasPieceAtPosition(move.toPosition)) {
            attacks.add(chessBoard.getPieceAtPosition(move.toPosition)!!)
          }
        }
      }
    }
    return attacks
  }

  private fun calculateProtectsForPiece(piece: Piece, chessBoard: ChessBoard): Set<Piece> {
    // todo: Implement
    return emptySet()
  }

  private fun Map<Piece, PieceRank>.mapToBoardRank(): Int {
    // todo: Implement
    return 0
  }

  data class PieceRank(
    /** How many moves could be done */
    val mobility: Int,
    /** How many opposite pieces are under attack */
    val attacks: Int,
    /** How many pieces are under protect */
    val protects: Int
  )
}

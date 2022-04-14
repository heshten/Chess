package com.heshten.engine

import com.heshten.core.board.ChessBoard
import com.heshten.core.models.Move
import com.heshten.core.models.PieceSide
import com.heshten.core.models.opposite
import com.heshten.core.models.pieces.*

class GameEngine(
  val engineSide: PieceSide,
  private val plies: Int,
  private val chessBoard: ChessBoard
) {

  fun calculateNextMove(): Move {
    val chessBoardCopy = chessBoard.copy()
    val randomMove = randomMove(chessBoardCopy, engineSide)
    val rankWithMove = searchMoveDfs(plies, randomMove, chessBoardCopy, engineSide)
    return rankWithMove.move
  }

  private fun randomMove(chessBoard: ChessBoard, pieceSide: PieceSide): Move {
    val resultMap = mutableSetOf<Move>()
    chessBoard.getAllPiecesForSide(pieceSide).toSet().forEach { piece ->
      resultMap.addAll(chessBoard.getPossibleMovesForPiece(piece))
    }
    return resultMap.random()
  }

  private fun calculateBoardRank(chessBoard: ChessBoard): BoardRank {
    if (
      chessBoard.isCheck(engineSide) &&
      chessBoard.hasNextMoves(engineSide).not()
    ) {
      // check and for engine
      return BoardRank(Int.MIN_VALUE, Int.MAX_VALUE)
    }
    if (
      chessBoard.isCheck(engineSide.opposite()) &&
      chessBoard.hasNextMoves(engineSide.opposite()).not()
    ) {
      // check and for opposite
      return BoardRank(Int.MAX_VALUE, Int.MAX_VALUE)
    }
    val ePieces = chessBoard.getAllPiecesForSide(engineSide)
    val oPieces = chessBoard.getAllPiecesForSide(engineSide.opposite())
    val ePiecesRanks = mutableMapOf<Piece, PieceRank>()
    val oPiecesRanks = mutableMapOf<Piece, PieceRank>()
    ePieces.forEach { piece -> ePiecesRanks[piece] = calculateRankForPiece(piece, chessBoard) }
    oPieces.forEach { piece -> oPiecesRanks[piece] = calculateRankForPiece(piece, chessBoard) }
    return BoardRank(
      engineRank = calculateRank(ePiecesRanks),
      oppositeRank = calculateRank(oPiecesRanks)
    )
  }

  private fun calculateRank(pieceRanks: Map<Piece, PieceRank>): Int {
    var resultRank = 0
    pieceRanks.forEach { entry ->
      val pieceWeight = entry.key.weight()
      val protectWeight = entry.value.protects.sumOf { it.weight() }
      val attacksWeight = entry.value.attacks.sumOf { it.weight() }
      resultRank += pieceWeight + protectWeight + attacksWeight
    }
    return resultRank
  }

  private fun searchMoveDfs(
    ply: Int,
    aMove: Move,
    chessBoard: ChessBoard,
    currentSide: PieceSide
  ): RankWithMove {
    if (ply == 0) {
      return RankWithMove(calculateBoardRank(chessBoard), aMove)
    }
    val eRank = if (currentSide == engineSide) Int.MIN_VALUE else Int.MAX_VALUE
    val oRank = if (currentSide == engineSide) Int.MAX_VALUE else Int.MIN_VALUE
    var maxRankWithMove = RankWithMove(BoardRank(eRank, oRank), aMove)
    getAllMovesForSide(chessBoard, currentSide).forEach { move ->
      chessBoard.doMove(move)
      val bestOppositeRankWithMove =
        searchMoveDfs(ply - 1, move, chessBoard, currentSide.opposite())
      if (bestOppositeRankWithMove.isBetterThan(currentSide, maxRankWithMove)) {
        maxRankWithMove = RankWithMove(bestOppositeRankWithMove.rank, move)
      }
      chessBoard.undo()
    }
    return maxRankWithMove
  }

  private fun RankWithMove.isBetterThan(
    side: PieceSide,
    other: RankWithMove
  ): Boolean {
    return if (side == engineSide) {
      rank.engineRank >= other.rank.engineRank &&
        rank.oppositeRank <= other.rank.oppositeRank
    } else {
      rank.engineRank <= other.rank.engineRank &&
        rank.oppositeRank >= other.rank.oppositeRank
    }
  }

  private fun getAllMovesForSide(chessBoard: ChessBoard, side: PieceSide): Set<Move> {
    val result = mutableSetOf<Move>()
    chessBoard.getAllPiecesForSide(side).forEach { piece ->
      val movesForPiece = chessBoard.getPossibleMovesForPiece(piece)
      result.addAll(movesForPiece)
    }
    return result
  }


  private fun calculateRankForPiece(piece: Piece, chessBoard: ChessBoard): PieceRank {
    return PieceRank(
      attacks = calculateAttacksForPiece(piece, chessBoard),
      protects = calculateProtectsForPiece(piece, chessBoard),
    )
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
    // todo
    return emptySet()
  }

  private fun Piece.weight(): Int = when (this) {
    is Pawn -> 5
    is Knight -> 10
    is Bishop -> 30
    is Rook -> 40
    is Queen -> 100
    is King -> 1
    else -> throw IllegalArgumentException()
  }

  data class PieceRank(
    /** How many opposite pieces are under attack */
    val attacks: Set<Piece>,
    /** How many pieces are under protect */
    val protects: Set<Piece>
  )

  data class RankWithMove(
    val rank: BoardRank,
    val move: Move
  )

  data class BoardRank(
    val engineRank: Int,
    val oppositeRank: Int
  )
}

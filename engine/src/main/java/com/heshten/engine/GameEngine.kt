package com.heshten.engine

import com.heshten.core.board.ChessBoard
import com.heshten.core.logic.PossibleMovesCalculator
import com.heshten.core.models.BoardPosition
import com.heshten.core.models.PieceSide
import com.heshten.core.models.opposite
import com.heshten.core.models.pieces.Piece

class GameEngine(
  val engineSide: PieceSide,
  private val maxSearchDepth: Int,
  private val chessBoard: ChessBoard,
  private val possibleMovesCalculator: PossibleMovesCalculator
) {

  fun calculateNextMove(): Move {
    val startMs = System.currentTimeMillis()
    populateMapRecursive(null, null, chessBoard, 0, maxSearchDepth)
    val endMs = System.currentTimeMillis()
    println(
      "populateMapRecursive(maxSearchDepth = $maxSearchDepth): ${(endMs - startMs) / 1000}s."
    )
    return randomMove(chessBoard, engineSide)
  }

  private fun randomMove(chessBoard: ChessBoard, pieceSide: PieceSide): Move {
    val resultMap = mutableSetOf<Move>()
    chessBoard.getAllPiecesForSide(pieceSide).forEach { piece ->
      val positions = possibleMovesCalculator.calculatePossibleMovesForPiece(piece, chessBoard)
      resultMap.addAll(positions.map { Move(piece.boardPosition, it) })
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

  private fun populateMapRecursive(
    engineMove: Move?,
    oppositeMove: Move?,
    chessBoard: ChessBoard,
    depth: Int,
    maxDepth: Int
  ) {
    if (engineMove != null && oppositeMove != null) {
//      val rankForEngine = calculateBoardRank(chessBoard, engineSide)
//      val rankForOpposite = calculateBoardRank(chessBoard, engineSide.opposite())
      // todo:
      // create and populate the graph during search with each move and represented rank
      // so that after graph traversing pick better move for engine.
    }
    if (depth >= maxDepth) {
      return
    }
    val chessBoardSnapshot = ChessBoard(chessBoard.getAllPieces())

    chessBoardSnapshot.getAllPiecesForSide(engineSide).forEach { enginePiece ->
      val possibleEngineMoves = possibleMovesCalculator
        .calculatePossibleMovesForPiece(enginePiece, chessBoardSnapshot)

      possibleEngineMoves.forEach { possibleEngineMovePosition ->
        val snapshot1 = ChessBoard(chessBoard.getAllPieces())
        val performedEngineMove =
          Move(enginePiece.boardPosition, possibleEngineMovePosition)
        snapshot1.selectPiece(enginePiece)
        snapshot1.moveSelectedPieceToPosition(possibleEngineMovePosition)

        snapshot1.getAllPiecesForSide(engineSide.opposite()).forEach { oppositePiece ->
          val possibleOppositeMoves = possibleMovesCalculator
            .calculatePossibleMovesForPiece(oppositePiece, snapshot1)

          possibleOppositeMoves.forEach { possibleOppositeMovePosition ->
            val snapshot2 = ChessBoard(snapshot1.getAllPieces())
            val performedOppositeMove =
              Move(oppositePiece.boardPosition, possibleOppositeMovePosition)
            snapshot2.selectPiece(oppositePiece)
            snapshot2.moveSelectedPieceToPosition(possibleOppositeMovePosition)

            populateMapRecursive(
              performedEngineMove,
              performedOppositeMove,
              snapshot2,
              depth + 1,
              maxSearchDepth
            )
          }
        }
      }
    }
  }

  private fun findPiecesUnderAttack(chessBoard: ChessBoard, side: PieceSide): Set<Piece> {
    val piecesUnderAttack = mutableSetOf<Piece>()
    chessBoard.getAllPiecesForSide(side.opposite()).forEach { piece ->
      if (possibleMovesCalculator.isUnderAttack(piece, chessBoard)) {
        piecesUnderAttack.add(piece)
      }
    }
    return piecesUnderAttack
  }

//  private fun getPieceRank(piece: Piece): Int {
//    return when (piece) {
//      is Bishop -> 25
//      is King -> 100
//      is Knight -> 15
//      is Pawn -> 5
//      is Queen -> 50
//      is Rook -> 35
//      else -> throw IllegalArgumentException()
//    }
//  }

  private fun calculateRankForPiece(piece: Piece, chessBoard: ChessBoard): PieceRank {
    return PieceRank(
      calculateMobilityForPiece(piece, chessBoard).size,
      calculateAttacksForPiece(piece, chessBoard).size,
      calculateProtectsForPiece(piece, chessBoard).size
    )
  }

  private fun calculateMobilityForPiece(piece: Piece, chessBoard: ChessBoard): Set<BoardPosition> {
    val mobility = mutableSetOf<BoardPosition>()
    val possibleMoves = possibleMovesCalculator.calculatePossibleMovesForPiece(piece, chessBoard)
    possibleMoves.forEach { movePosition ->
      if (!chessBoard.hasPieceAtPosition(movePosition)) {
        mobility.add(movePosition)
      }
    }
    return mobility
  }

  private fun calculateAttacksForPiece(piece: Piece, chessBoard: ChessBoard): Set<Piece> {
    val attacks = mutableSetOf<Piece>()
    val possibleMoves = possibleMovesCalculator.calculatePossibleMovesForPiece(piece, chessBoard)
    possibleMoves.forEach { movePosition ->
      if (chessBoard.hasPieceAtPosition(movePosition)) {
        attacks.add(chessBoard.getPieceAtPosition(movePosition)!!)
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

  data class Move(
    val fromPosition: BoardPosition,
    val toPosition: BoardPosition
  )
}

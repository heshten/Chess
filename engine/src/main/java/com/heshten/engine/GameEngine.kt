package com.heshten.engine

import com.heshten.core.board.ChessBoard
import com.heshten.core.logic.PossibleMovesCalculator
import com.heshten.core.models.BoardPosition
import com.heshten.core.models.PieceSide
import com.heshten.core.models.opposite
import com.heshten.core.models.pieces.*

class GameEngine(
  val engineSide: PieceSide,
  private val maxSearchDepth: Int,
  private val chessBoard: ChessBoard,
  private val possibleMovesCalculator: PossibleMovesCalculator
) {

  fun calculateNextMove(): Move {
//    populateMapRecursive(null, null, chessBoard, 0, maxSearchDepth)
    return randomMove()
  }

  private fun randomMove(): Move {
    val resultMap = mutableSetOf<Move>()
    val enginePieces = chessBoard.getAllPieces().filter { it.pieceSide == engineSide }
    enginePieces.forEach { piece ->
      val positions = possibleMovesCalculator.calculatePossibleMovesForPiece(piece, chessBoard)
      resultMap.addAll(positions.map { Move(piece.boardPosition, it) })
    }
    return resultMap.random()
  }

  private fun calculateBoardRank(chessBoard: ChessBoard, side: PieceSide): Int {
    // find all possible attacks
    val currentPiecesUnderAttack = findPiecesUnderAttack(chessBoard, side)
    val oppositePiecesUnderAttack = findPiecesUnderAttack(chessBoard, side.opposite())
    var rank = 0
    currentPiecesUnderAttack.forEach { piece -> rank -= getPieceRank(piece) }
    oppositePiecesUnderAttack.forEach { piece -> rank += getPieceRank(piece) }
    return rank
  }

  private fun populateMapRecursive(
    engineMove: Move?,
    oppositeMove: Move?,
    chessBoard: ChessBoard,
    depth: Int,
    maxDepth: Int
  ) {
    if (engineMove != null && oppositeMove != null) {
      val rankForEngine = calculateBoardRank(chessBoard, engineSide)
      val rankForOpposite = calculateBoardRank(chessBoard, engineSide.opposite())
      // todo:
      // create and populate the graph during search with each move and represented rank
      // so that after graph traversing pick better move for engine.
    }
    if (depth >= maxDepth) {
      return
    }
    val chessBoardSnapshot = ChessBoard(chessBoard.getAllPieces())

    chessBoardSnapshot.getAllPieces().filter { it.pieceSide == engineSide }.forEach { enginePiece ->
      val possibleEngineMoves = possibleMovesCalculator
        .calculatePossibleMovesForPiece(enginePiece, chessBoardSnapshot)

      possibleEngineMoves.forEach { possibleEngineMovePosition ->
        val snapshot1 = ChessBoard(chessBoard.getAllPieces())
        val performedEngineMove =
          Move(enginePiece.boardPosition, possibleEngineMovePosition)
        snapshot1.selectPiece(enginePiece)
        snapshot1.moveSelectedPieceToPosition(possibleEngineMovePosition)

        snapshot1.getAllPieces().filter { it.pieceSide != engineSide }.forEach { oppositePiece ->
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
    val pieces = chessBoard.getAllPieces().filter { it.pieceSide != side }
    val piecesUnderAttack = mutableSetOf<Piece>()
    pieces.forEach { piece ->
      if (possibleMovesCalculator.isUnderAttack(piece, chessBoard)) {
        piecesUnderAttack.add(piece)
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

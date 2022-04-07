package com.heshten.chess.core

import android.content.res.Resources
import com.heshten.chess.core.board.ChessBoard
import com.heshten.chess.core.board.NewGameBoardCreator
import com.heshten.chess.core.logic.MoveChecker
import com.heshten.chess.core.logic.TakeChecker
import com.heshten.chess.core.logic.facedes.MoveCheckerFacade
import com.heshten.chess.core.logic.facedes.TakeCheckerFacade
import com.heshten.chess.core.logic.movecheckers.DiagonalMovesChecker
import com.heshten.chess.core.logic.movecheckers.HorizontalMovesChecker
import com.heshten.chess.core.logic.movecheckers.KnightLikeMovesChecker
import com.heshten.chess.core.logic.movecheckers.VerticalMovesChecker
import com.heshten.chess.core.logic.takecheckers.DiagonalTakeChecker
import com.heshten.chess.core.logic.takecheckers.HorizontalTakeChecker
import com.heshten.chess.core.logic.takecheckers.KnightLikeTakeChecker
import com.heshten.chess.core.logic.takecheckers.VerticalTakeChecker
import com.heshten.chess.core.models.BoardPosition
import com.heshten.chess.core.models.PieceSide
import com.heshten.chess.core.models.pieces.Piece
import com.heshten.chess.core.recources.BlackPiecesResourceProvider
import com.heshten.chess.core.recources.WhitePiecesResourceProvider
import com.heshten.chess.core.validator.SideMoveValidator

class Game(
  private val chessBoard: ChessBoard,
  private val movesCheckerFacade: MoveChecker,
  private val takesCheckerFacade: TakeChecker,
  private val sideMoveValidator: SideMoveValidator,
  private val redrawBoard: (ChessBoard) -> Unit
) {

  companion object {

    fun createNewGame(
      resources: Resources,
      bottomSide: PieceSide,
      updateBoardCallback: (ChessBoard) -> Unit
    ): Game {
      val topSide = if (bottomSide == PieceSide.WHITE) PieceSide.BLACK else PieceSide.WHITE
      //di
      val bResourceProvider = BlackPiecesResourceProvider(resources)
      val wResourceProvider = WhitePiecesResourceProvider(resources)
      val newGameBoardCreator = NewGameBoardCreator(wResourceProvider, bResourceProvider)
      val board = ChessBoard(newGameBoardCreator.createNewBoard(topSide, bottomSide))
      val horizontalMovesChecker = HorizontalMovesChecker(board)
      val knightLikeMovesChecker = KnightLikeMovesChecker(board)
      val verticalMovesChecker = VerticalMovesChecker(board)
      val diagonalMovesChecker = DiagonalMovesChecker(board)
      val horizontalTakesChecker = HorizontalTakeChecker(board)
      val knightLikeTakesChecker = KnightLikeTakeChecker(board)
      val verticalTakesChecker = VerticalTakeChecker(board)
      val diagonalTakesChecker = DiagonalTakeChecker(board)
      val sideValidator = SideMoveValidator()
      val moveCheckerFacade = MoveCheckerFacade(
        horizontalMovesChecker,
        verticalMovesChecker,
        diagonalMovesChecker,
        knightLikeMovesChecker
      )
      val takeCheckerFacade = TakeCheckerFacade(
        diagonalTakesChecker,
        horizontalTakesChecker,
        knightLikeTakesChecker,
        verticalTakesChecker
      )
      return Game(
        board,
        moveCheckerFacade,
        takeCheckerFacade,
        sideValidator,
        updateBoardCallback
      )
    }
  }

  init {
    redrawBoard()
  }

  fun onPositionTouched(boardPosition: BoardPosition) {
    val pieceAtPosition = chessBoard.getPieceAtPosition(boardPosition)
    if (pieceAtPosition == null) {
      if (chessBoard.isPossibleMoveTo(boardPosition)) {
        moveSelectedPieceToPosition(boardPosition)
      }
    } else {
      if (chessBoard.isPossibleMoveTo(boardPosition)) {
        moveSelectedPieceToPosition(boardPosition)
      } else {
        selectPiece(pieceAtPosition)
      }
    }
  }

  private fun selectPiece(piece: Piece) {
    if (sideMoveValidator.getCurrentSide() == piece.pieceSide) {
      chessBoard.setPossibleMoves(getPossibleMovesForPiece(piece))
      chessBoard.selectPiece(piece)
      redrawBoard()
    }
  }

  private fun moveSelectedPieceToPosition(boardPosition: BoardPosition) {
    if (chessBoard.hasPieceAtPosition(boardPosition)) {
      //take piece
      chessBoard.removePieceAtPosition(boardPosition)
    }
    chessBoard.moveSelectedPieceToPosition(boardPosition)
    chessBoard.clearPossibleMovesPositions()
    sideMoveValidator.changeSide()
    redrawBoard()
  }

  private fun getPossibleMovesForPiece(piece: Piece): Set<BoardPosition> {
    val possibleMoves = mutableSetOf<BoardPosition>()
    possibleMoves.addAll(movesCheckerFacade.getPossibleMoves(piece))
    possibleMoves.addAll(takesCheckerFacade.getPossibleTakes(piece))
    return possibleMoves
  }

  private fun redrawBoard() {
    redrawBoard.invoke(chessBoard)
  }
}

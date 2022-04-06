package com.heshten.chess.ui

import android.content.res.Resources
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.heshten.chess.core.Game
import com.heshten.chess.core.board.ChessBoard
import com.heshten.chess.core.board.NewGameBoardCreator
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

class MainViewModel : ViewModel() {

  private lateinit var game: Game

  private val _updateBoardEvent = MutableLiveData<Unit>()
  val updateBoardEvent: LiveData<Unit> = _updateBoardEvent

  private val _chessBoard = MutableLiveData<ChessBoard>()
  val chessBoard: LiveData<ChessBoard> = _chessBoard

  fun startNewGame(resources: Resources, bottomSide: PieceSide) {
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
    game = Game(board, moveCheckerFacade, takeCheckerFacade, sideValidator, redrawBoard = {
      _updateBoardEvent.value = Unit
    })
    _chessBoard.value = board
  }

  fun onPieceSelected(piece: Piece) {
    game.selectPiece(piece)
  }

  fun moveSelectedPieceToPosition(boardPosition: BoardPosition) {
    game.moveSelectedPieceToPosition(boardPosition)
  }
}

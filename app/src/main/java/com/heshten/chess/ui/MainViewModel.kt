package com.heshten.chess.ui

import android.app.Application
import android.graphics.Bitmap
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.heshten.chess.ui.recources.PieceResourceProvider
import com.heshten.chess.ui.views.BoardView
import com.heshten.core.Game
import com.heshten.core.NewGameBoardCreator
import com.heshten.core.board.ChessBoard
import com.heshten.core.board.PossibleMovesCalculator
import com.heshten.core.logic.facedes.MoveCheckerFacade
import com.heshten.core.logic.facedes.TakeCheckerFacade
import com.heshten.core.logic.movecheckers.DiagonalMovesChecker
import com.heshten.core.logic.movecheckers.HorizontalMovesChecker
import com.heshten.core.logic.movecheckers.KnightLikeMovesChecker
import com.heshten.core.logic.movecheckers.VerticalMovesChecker
import com.heshten.core.logic.takecheckers.DiagonalTakeChecker
import com.heshten.core.logic.takecheckers.HorizontalTakeChecker
import com.heshten.core.logic.takecheckers.KnightLikeTakeChecker
import com.heshten.core.logic.takecheckers.VerticalTakeChecker
import com.heshten.core.models.BoardPosition
import com.heshten.core.models.Move
import com.heshten.core.models.PieceSide
import com.heshten.core.models.opposite
import com.heshten.core.models.pieces.*
import com.heshten.core.validator.SideMoveValidator
import com.heshten.engine.GameEngine
import java.util.concurrent.Executor

class MainViewModel(
  app: Application,
  private val mainThreadExecutor: Executor,
  private val engineMoveExecutorService: Executor,
  private val blackPieceResourceProvider: PieceResourceProvider,
  private val whitePieceResourceProvider: PieceResourceProvider
) : AndroidViewModel(app) {

  private var game: Game? = null
  private var engine: GameEngine? = null

  private val _currentSide = MutableLiveData<PieceSide>()
  val currentSide: LiveData<PieceSide> = _currentSide

  private val _gameResult = MutableLiveData<GameResultUIModel>()
  val gameResult: LiveData<GameResultUIModel> = _gameResult

  private val _boardPieces = MutableLiveData<Map<BoardPosition, BoardView.BoardPiece>>()
  val boardPieces: LiveData<Map<BoardPosition, BoardView.BoardPiece>> = _boardPieces

  private val _boardTouchEnable = MutableLiveData(false)
  val boardTouchEnable: LiveData<Boolean> = _boardTouchEnable

  fun startNewGame(playerSide: PieceSide) {
    val topSide = playerSide.opposite()
    //di
    val newGameBoardCreator = NewGameBoardCreator()
    val piecesSnapshot = newGameBoardCreator.createNewBoard(topSide, playerSide)
    val horizontalMovesChecker = HorizontalMovesChecker()
    val knightLikeMovesChecker = KnightLikeMovesChecker()
    val verticalMovesChecker = VerticalMovesChecker()
    val diagonalMovesChecker = DiagonalMovesChecker()
    val horizontalTakesChecker = HorizontalTakeChecker()
    val knightLikeTakesChecker = KnightLikeTakeChecker()
    val verticalTakesChecker = VerticalTakeChecker()
    val diagonalTakesChecker = DiagonalTakeChecker()
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
    val moveCalc = PossibleMovesCalculator(moveCheckerFacade, takeCheckerFacade)
    val board = ChessBoard(piecesSnapshot, moveCalc)
    engine = GameEngine(topSide, 2, board)
    game = Game(
      playerSide,
      board,
      sideValidator,
      ::performMoveForSide,
      ::updateBoard,
      ::onGameFinished,
    )
  }

  fun clearResultShowFlag() {
    val currentValue = gameResult.value
    if (currentValue != null) {
      _gameResult.value = currentValue.copy(showDialog = false)
    }
  }

  fun doMove(move: Move) {
    game?.doMove(move)
  }

  fun undo() {
    game?.undo()
  }

  private fun performMoveForSide(side: PieceSide) {
    if (side == engine?.engineSide) {
      performEngineMove()
    } else {
      _boardTouchEnable.value = true
    }
  }

  private fun onGameFinished(result: Game.GameResult) {
    _boardTouchEnable.value = false
    _gameResult.value = GameResultUIModel(result, true)
  }

  private fun updateBoard(redrawModel: Game.RedrawModel) {
    val mutableUnitsMap = mutableMapOf<BoardPosition, BoardView.BoardPiece>()
    redrawModel.chessBoardData.forEach { configEntry ->
      val pieceBitmap = getBitmapForPiece(configEntry.key)
      mutableUnitsMap[configEntry.key.boardPosition] =
        BoardView.BoardPiece(pieceBitmap, configEntry.value)
    }
    _boardPieces.value = mutableUnitsMap
    _currentSide.value = redrawModel.currentMoveSide
  }

  private fun performEngineMove() {
    val engineLocal = engine ?: return
    _boardTouchEnable.value = false
    engineMoveExecutorService.execute {
      val move = engineLocal.calculateNextMove()
      mainThreadExecutor.execute { doMove(move) }
    }
  }

  private fun getBitmapForPiece(piece: Piece): Bitmap {
    val resources = getApplication<Application>().resources
    val resourceProvider = when (piece.pieceSide) {
      PieceSide.WHITE -> whitePieceResourceProvider
      PieceSide.BLACK -> blackPieceResourceProvider
    }
    return when (piece) {
      is Bishop -> resourceProvider.getBishopBitmap(resources)
      is King -> resourceProvider.getKingBitmap(resources)
      is Knight -> resourceProvider.getKnightBitmap(resources)
      is Pawn -> resourceProvider.getPawnBitmap(resources)
      is Queen -> resourceProvider.getQueenBitmap(resources)
      is Rook -> resourceProvider.getRookBitmap(resources)
      else -> throw IllegalArgumentException()
    }
  }

  data class GameResultUIModel(
    val gameResult: Game.GameResult,
    val showDialog: Boolean
  )
}

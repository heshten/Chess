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
    val sideValidator = SideMoveValidator(::onSideChanged)
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
    game = Game(
      topSide.opposite(),
      board,
      sideValidator,
      ::updateBoard,
      ::onGameFinished,
    )
    engine = GameEngine(topSide, 1, board)
    if (topSide == PieceSide.WHITE) {
      performEngineMove()
    } else {
      _boardTouchEnable.value = true
    }
  }

  fun clearResultShowFlag() {
    val currentValue = gameResult.value
    if (currentValue != null) {
      _gameResult.value = currentValue.copy(showDialog = false)
    }
  }

  fun undo() {
    val gameLocal = game ?: return
    gameLocal.undo()
  }

  fun doMove(move: Move) {
    game?.doMove(move)
  }

  private fun onSideChanged(side: PieceSide) {
    _currentSide.value = side
    if (side == engine?.engineSide) {
      performEngineMove()
    }
  }

  private fun onGameFinished(result: Game.GameResult) {
    _boardTouchEnable.value = false
    _gameResult.value = GameResultUIModel(result, true)
  }

  private fun updateBoard(boardConfig: Map<Piece, Set<Move>>) {
    val mutableUnitsMap = mutableMapOf<BoardPosition, BoardView.BoardPiece>()
    boardConfig.forEach { configEntry ->
      val pieceBitmap = getBitmapForPiece(configEntry.key)
      mutableUnitsMap[configEntry.key.boardPosition] =
        BoardView.BoardPiece(pieceBitmap, configEntry.value)
    }
    _boardPieces.value = mutableUnitsMap
  }

  private fun performEngineMove() {
    val engineLocal = engine ?: return
    _boardTouchEnable.value = false
    engineMoveExecutorService.execute {
      val move = engineLocal.calculateNextMove()
      mainThreadExecutor.execute {
        doMove(move)
        _boardTouchEnable.value = true
      }
    }
  }

  private fun getBitmapForPiece(piece: Piece): Bitmap {
    val resources = getApplication<Application>().resources
    val resourceProvider = getResourceProviderForSide(piece.pieceSide)
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

  private fun getResourceProviderForSide(side: PieceSide): PieceResourceProvider {
    return when (side) {
      PieceSide.WHITE -> whitePieceResourceProvider
      PieceSide.BLACK -> blackPieceResourceProvider
    }
  }

  data class GameResultUIModel(
    val gameResult: Game.GameResult,
    val showDialog: Boolean
  )
}

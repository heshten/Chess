package com.heshten.chess.ui

import android.graphics.Bitmap
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.heshten.chess.ui.recources.PieceResourceProvider
import com.heshten.chess.ui.views.BoardView
import com.heshten.core.Game
import com.heshten.core.NewGameBoardCreator
import com.heshten.core.board.ChessBoard
import com.heshten.core.logic.PossibleMovesCalculator
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
import com.heshten.core.models.PieceSide
import com.heshten.core.models.opposite
import com.heshten.core.models.pieces.*
import com.heshten.core.validator.SideMoveValidator
import com.heshten.engine.GameEngine
import java.util.concurrent.Executor
import java.util.concurrent.ExecutorService

class MainViewModel(
  private val mainThreadExecutor: Executor,
  private val engineMoveExecutorService: ExecutorService,
  private val blackPieceResourceProvider: PieceResourceProvider,
  private val whitePieceResourceProvider: PieceResourceProvider
) : ViewModel() {

  private var game: Game? = null
  private var engine: GameEngine? = null

  private val _currentSide = MutableLiveData<PieceSide>()
  val currentSide: LiveData<PieceSide> = _currentSide

  private val _gameResult = MutableLiveData<GameResultUIModel>()
  val gameResult: LiveData<GameResultUIModel> = _gameResult

  private val _boardUnits = MutableLiveData<Map<BoardPosition, BoardView.BoardUnit>>()
  val boardUnits: LiveData<Map<BoardPosition, BoardView.BoardUnit>> = _boardUnits

  private val _lockBoardForUser = MutableLiveData(true)
  val lockBoardForUser: LiveData<Boolean> = _lockBoardForUser

  fun startNewGame(playerSide: PieceSide) {
    val topSide = playerSide.opposite()
    //di
    val newGameBoardCreator = NewGameBoardCreator()
    val board = ChessBoard(newGameBoardCreator.createNewBoard(topSide, playerSide))
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
    val possibleMovesCalculator = PossibleMovesCalculator(moveCheckerFacade, takeCheckerFacade)
    game = Game(
      board,
      possibleMovesCalculator,
      sideValidator,
      ::updateBoard,
      ::onGameFinished,
    )
    engine = GameEngine(topSide, 1, board, possibleMovesCalculator)
    if (topSide == PieceSide.WHITE) {
      performEngineMove()
    } else {
      _lockBoardForUser.value = false
    }
  }

  fun clearResultShowFlag() {
    val currentValue = gameResult.value
    if (currentValue != null) {
      _gameResult.value = currentValue.copy(showDialog = false)
    }
  }

  fun onPositionTouched(boardPosition: BoardPosition) {
    game?.onPositionTouched(boardPosition)
  }

  private fun onSideChanged(side: PieceSide) {
    _currentSide.value = side
    if (side == engine?.engineSide) {
      performEngineMove()
    }
  }

  private fun onGameFinished(result: Game.GameResult) {
    _lockBoardForUser.value = true
    _gameResult.value = GameResultUIModel(result, true)
  }

  private fun updateBoard(pieces: Set<Piece>, possibleMovePositions: Set<BoardPosition>) {
    val mutableUnitsMap = mutableMapOf<BoardPosition, BoardView.BoardUnit>()
    pieces.forEach { piece ->
      mutableUnitsMap[piece.boardPosition] =
        BoardView.BoardUnit.Piece(false, getBitmapForPiece(piece))
    }
    possibleMovePositions.forEach { boardPosition ->
      val existingBoardUnit = mutableUnitsMap[boardPosition]
      if (existingBoardUnit != null && existingBoardUnit is BoardView.BoardUnit.Piece) {
        mutableUnitsMap[boardPosition] = existingBoardUnit.copy(isHighlighted = true)
      } else {
        mutableUnitsMap[boardPosition] = BoardView.BoardUnit.Dot
      }
    }
    _boardUnits.value = mutableUnitsMap
  }

  private fun performEngineMove() {
    val engineLocal = engine ?: return
    _lockBoardForUser.value = true
    engineMoveExecutorService.execute {
      val nextMove = engineLocal.calculateNextMove()
      mainThreadExecutor.execute {
        _lockBoardForUser.value = false
        // simulate the move
        onPositionTouched(nextMove.fromPosition)
        onPositionTouched(nextMove.toPosition)
      }
    }
  }

  private fun getBitmapForPiece(piece: Piece): Bitmap {
    val resourceProvider = getResourceProviderForSide(piece.pieceSide)
    return when (piece) {
      is Bishop -> resourceProvider.getBishopBitmap()
      is King -> resourceProvider.getKingBitmap()
      is Knight -> resourceProvider.getKnightBitmap()
      is Pawn -> resourceProvider.getPawnBitmap()
      is Queen -> resourceProvider.getQueenBitmap()
      is Rook -> resourceProvider.getRookBitmap()
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

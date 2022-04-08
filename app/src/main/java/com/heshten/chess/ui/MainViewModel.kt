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
import com.heshten.core.models.pieces.*
import com.heshten.core.validator.SideMoveValidator
import com.heshten.engine.GameEngine

class MainViewModel(
  private val blackPieceResourceProvider: PieceResourceProvider,
  private val whitePieceResourceProvider: PieceResourceProvider
) : ViewModel() {

  private var game: Game? = null
  private var engine: GameEngine? = null

  private val _currentSide = MutableLiveData<PieceSide>()
  val currentSide: LiveData<PieceSide> = _currentSide

  private val _boardUnits = MutableLiveData<Map<BoardPosition, BoardView.BoardUnit>>()
  val boardUnits: LiveData<Map<BoardPosition, BoardView.BoardUnit>> = _boardUnits

  fun startNewGame(playerSide: PieceSide) {
    val topSide = if (playerSide == PieceSide.WHITE) PieceSide.BLACK else PieceSide.WHITE
    //di
    val newGameBoardCreator = NewGameBoardCreator()
    val board = ChessBoard(newGameBoardCreator.createNewBoard(topSide, playerSide))
    val horizontalMovesChecker = HorizontalMovesChecker(board)
    val knightLikeMovesChecker = KnightLikeMovesChecker(board)
    val verticalMovesChecker = VerticalMovesChecker(board)
    val diagonalMovesChecker = DiagonalMovesChecker(board)
    val horizontalTakesChecker = HorizontalTakeChecker(board)
    val knightLikeTakesChecker = KnightLikeTakeChecker(board)
    val verticalTakesChecker = VerticalTakeChecker(board)
    val diagonalTakesChecker = DiagonalTakeChecker(board)
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
    game = Game(
      board,
      moveCheckerFacade,
      takeCheckerFacade,
      sideValidator,
      ::updateBoard,
    )
    engine = GameEngine(game!!, board, topSide, sideValidator)
    if (topSide == PieceSide.WHITE) {
      engine?.performMove()
    }
  }

  fun onPositionTouched(boardPosition: BoardPosition) {
    game?.onPositionTouched(boardPosition)
  }

  private fun onSideChanged(side: PieceSide) {
    _currentSide.value = side
    engine?.performMove()
  }

  private fun updateBoard(chessBoard: ChessBoard) {
    val mutableUnitsMap = mutableMapOf<BoardPosition, BoardView.BoardUnit>()
    chessBoard.getAllPieces().forEach { piece ->
      mutableUnitsMap[piece.getCurrentPosition()] =
        BoardView.BoardUnit.Piece(false, getBitmapForPiece(piece))
    }
    chessBoard.getPossibleMovesPositions().forEach { boardPosition ->
      val existingBoardUnit = mutableUnitsMap[boardPosition]
      if (existingBoardUnit != null && existingBoardUnit is BoardView.BoardUnit.Piece) {
        mutableUnitsMap[boardPosition] = existingBoardUnit.copy(isHighlighted = true)
      } else {
        mutableUnitsMap[boardPosition] = BoardView.BoardUnit.Dot
      }
    }
    _boardUnits.value = mutableUnitsMap
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
}

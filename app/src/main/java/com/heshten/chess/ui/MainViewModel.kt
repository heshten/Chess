package com.heshten.chess.ui

import android.content.res.Resources
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.heshten.chess.core.Game
import com.heshten.chess.core.board.ChessBoard
import com.heshten.chess.core.models.BoardPosition
import com.heshten.chess.core.models.PieceSide
import com.heshten.chess.ui.views.BoardView

class MainViewModel : ViewModel() {

  private lateinit var game: Game

  private val _chessUnits = MutableLiveData<Map<BoardPosition, BoardView.BoardUnit>>()
  val chessUnits: LiveData<Map<BoardPosition, BoardView.BoardUnit>> = _chessUnits

  fun startNewGame(resources: Resources, bottomSide: PieceSide) {
    game = Game.createNewGame(resources, bottomSide, ::updateBoard)
  }

  fun onPositionTouched(boardPosition: BoardPosition) {
    game.onPositionTouched(boardPosition)
  }

  private fun updateBoard(chessBoard: ChessBoard) {
    val mutableUnitsMap = mutableMapOf<BoardPosition, BoardView.BoardUnit>()
    chessBoard.getAllPieces().forEach { piece ->
      mutableUnitsMap[piece.getCurrentPosition()] =
        BoardView.BoardUnit.Piece(false, piece.bitmap)
    }
    chessBoard.getPossibleMovesPositions().forEach { boardPosition ->
      val existingBoardUnit = mutableUnitsMap[boardPosition]
      if (existingBoardUnit != null && existingBoardUnit is BoardView.BoardUnit.Piece) {
        mutableUnitsMap[boardPosition] = existingBoardUnit.copy(isHighlighted = true)
      } else {
        mutableUnitsMap[boardPosition] = BoardView.BoardUnit.Dot
      }
    }
    _chessUnits.value = mutableUnitsMap
  }
}

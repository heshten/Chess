package com.heshten.chess.ui

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.heshten.chess.R
import com.heshten.chess.core.models.BoardPosition
import com.heshten.chess.core.models.PieceSide
import com.heshten.chess.core.models.pieces.Piece
import com.heshten.chess.ui.views.listeners.OnPieceSelectListener
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), OnPieceSelectListener {

  private lateinit var viewModel: MainViewModel

  override fun onCreate(savedInstanceState: Bundle?) {
    initViewModel()
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)
    setListeners()
    viewModel.chessBoard.observe(this, Observer { chessBoard ->
      boardView.setChessBoard(chessBoard)
    })
    viewModel.updateBoardEvent.observe(this, Observer {
      boardView.redrawBoard()
    })
  }

  override fun onPieceSelected(piece: Piece) {
    Log.d("some_tag", "onPieceSelected")
    viewModel.onPieceSelected(piece)
  }

  override fun moveSelectedPieceToPosition(boardPosition: BoardPosition) {
    Log.d("some_tag", "moveSelectedPieceToPosition: $boardPosition")
    viewModel.moveSelectedPieceToPosition(boardPosition)
  }

  private fun setListeners() {
    boardView.setPieceSelectListener(this)
    btnNewGame.setOnClickListener {
      val bottomSide = when (sideSwitch.isChecked) {
        true -> PieceSide.WHITE
        false -> PieceSide.BLACK
      }
      viewModel.startNewGame(resources, bottomSide)
    }
  }

  private fun initViewModel() {
    viewModel = ViewModelProvider(this).get(MainViewModel::class.java)
  }
}

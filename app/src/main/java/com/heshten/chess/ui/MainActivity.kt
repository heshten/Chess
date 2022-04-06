package com.heshten.chess.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.heshten.chess.R
import com.heshten.chess.core.models.BoardPosition
import com.heshten.chess.core.models.PieceSide
import com.heshten.chess.ui.views.BoardView
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), BoardView.OnPositionTouchListener {

  private lateinit var viewModel: MainViewModel

  override fun onCreate(savedInstanceState: Bundle?) {
    initViewModel()
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)
    setListeners()
    viewModel.chessUnits.observe(this, Observer { chessUnits ->
      boardView.submitUnits(chessUnits)
    })
  }

  override fun onPositionTouched(boardPosition: BoardPosition) {
    viewModel.onPositionTouched(boardPosition)
  }

  private fun setListeners() {
    boardView.setOnPositionTouchListener(this)
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

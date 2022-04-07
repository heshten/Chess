package com.heshten.chess.ui

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.math.MathUtils
import com.heshten.chess.R
import com.heshten.chess.core.models.BoardPosition
import com.heshten.chess.core.models.PieceSide
import com.heshten.chess.ui.views.BoardView
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity :
  AppCompatActivity(R.layout.activity_main), BoardView.OnPositionTouchListener {

  private lateinit var viewModel: MainViewModel

  override fun onCreate(savedInstanceState: Bundle?) {
    initViewModel()
    super.onCreate(savedInstanceState)
    setListeners()
    setupBottomSheet()
    viewModel.boardUnits.observe(this, { boardUnits ->
      boardView.submitUnits(boardUnits)
    })
  }

  override fun onPositionTouched(boardPosition: BoardPosition) {
    viewModel.onPositionTouched(boardPosition)
  }

  private fun setListeners() {
    boardView.setOnPositionTouchListener(this)
    btnNewGame.setOnClickListener {
      viewModel.startNewGame(resources, PieceSide.WHITE)
      collapseBottomSheet()
    }
    btnNewGame2.setOnClickListener {
      viewModel.startNewGame(resources, PieceSide.WHITE)
      collapseBottomSheet()
    }
    btnNewGame3.setOnClickListener {
      viewModel.startNewGame(resources, PieceSide.WHITE)
      collapseBottomSheet()
    }
  }

  private fun setupBottomSheet() {
    val behavior = BottomSheetBehavior.from(menuSheet)
    behavior.peekHeight = resources.getDimensionPixelOffset(R.dimen.dp_24)
    behavior.addBottomSheetCallback(MenuSheetCallback())
  }

  private fun collapseBottomSheet() {
    val behavior = BottomSheetBehavior.from(menuSheet)
    behavior.state = BottomSheetBehavior.STATE_COLLAPSED
  }

  private fun initViewModel() {
    viewModel = ViewModelProvider(this).get(MainViewModel::class.java)
  }

  private inner class MenuSheetCallback : BottomSheetBehavior.BottomSheetCallback() {
    override fun onStateChanged(bottomSheet: View, newState: Int) {
    }

    override fun onSlide(bottomSheet: View, slideOffset: Float) {
      val alphaScale = MathUtils.lerp(1f, 0.75f, slideOffset)
      val translation = MathUtils.lerp(0f, -(boardView.height / 4f), slideOffset)
      // does the property changes at once
      boardView.animate()
        .translationY(translation)
        .scaleX(alphaScale)
        .scaleY(alphaScale)
        .alpha(alphaScale)
        .setDuration(0)
        .start()
    }
  }
}

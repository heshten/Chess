package com.heshten.chess.ui

import android.content.res.Resources
import android.graphics.Bitmap
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.math.MathUtils
import com.heshten.chess.R
import com.heshten.chess.ui.recources.BlackPiecesResourceProvider
import com.heshten.chess.ui.recources.WhitePiecesResourceProvider
import com.heshten.chess.ui.views.BoardView
import com.heshten.core.models.BoardPosition
import com.heshten.core.models.PieceSide
import kotlinx.android.synthetic.main.activity_main.*
import java.util.concurrent.Executor
import java.util.concurrent.Executors

class MainActivity :
  AppCompatActivity(R.layout.activity_main), BoardView.OnPositionTouchListener {

  private lateinit var viewModel: MainViewModel

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    initViewModel()
    setListeners()
    setupBottomSheet()
    viewModel.currentSide.observe(this, { currentSide ->
      tvSide.text = currentSide.name
    })
    viewModel.boardUnits.observe(this, { boardUnits ->
      boardView.submitUnits(boardUnits)
    })
    viewModel.lockBoardForUser.observe(this, { lockForUser ->
      boardView.setUserInteractionAvailable(!lockForUser)
    })
    viewModel.gameResult.observe(this, { result ->
      if (result.showDialog) {
        AlertDialog.Builder(this)
          .setTitle("Game has ended!")
          .setMessage("Winner: ${result.winner.name}")
          .setCancelable(true)
          .setOnCancelListener { viewModel.clearResultShowFlag() }
          .create()
          .show()
      }
    })
  }

  override fun onPositionTouched(boardPosition: BoardPosition) {
    viewModel.onPositionTouched(boardPosition)
  }

  private fun setListeners() {
    boardView.setOnPositionTouchListener(this)
    btnNewGameBlack.setOnClickListener {
      viewModel.startNewGame(PieceSide.BLACK)
      collapseBottomSheet()
    }
    btnNewGameWhite.setOnClickListener {
      viewModel.startNewGame(PieceSide.WHITE)
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
    val factory = MainViewModelFactory(
      Executors.newSingleThreadExecutor(),
      MainThreadExecutor(),
      mutableMapOf(), resources
    )
    viewModel = ViewModelProvider(this, factory).get(MainViewModel::class.java)
  }

  private inner class MenuSheetCallback : BottomSheetBehavior.BottomSheetCallback() {
    override fun onStateChanged(bottomSheet: View, newState: Int) {
    }

    override fun onSlide(bottomSheet: View, slideOffset: Float) {
      val alphaScale = MathUtils.lerp(1f, 0.75f, slideOffset)
      val translation = MathUtils.lerp(0f, -(boardView.height / 4f), slideOffset)
      // does the property changes at once
      boardView.animate()
        .setDuration(0)
        .translationY(translation)
        .scaleX(alphaScale)
        .scaleY(alphaScale)
        .alpha(alphaScale)
        .start()
      tvSide.animate()
        .setDuration(0)
        .scaleX(alphaScale)
        .scaleY(alphaScale)
        .alpha(alphaScale)
        .start()
    }
  }

  private class MainViewModelFactory(
    private val engineExecutor: Executor,
    private val mainThreadExecutor: Executor,
    private val cache: MutableMap<Int, Bitmap>,
    private val resources: Resources
  ) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
      return MainViewModel(
        engineExecutor,
        mainThreadExecutor,
        BlackPiecesResourceProvider(cache, resources),
        WhitePiecesResourceProvider(cache, resources)
      ) as T
    }
  }

  private class MainThreadExecutor : Executor {

    private val handler = Handler(Looper.getMainLooper())

    override fun execute(command: Runnable?) {
      if (command != null) {
        handler.post(command)
      }
    }
  }
}

package com.heshten.chess.ui

import android.app.Application
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
import com.heshten.core.Game
import com.heshten.core.models.Move
import com.heshten.core.models.PieceSide
import kotlinx.android.synthetic.main.activity_main.*
import java.util.concurrent.Executor
import java.util.concurrent.Executors

class MainActivity :
  AppCompatActivity(R.layout.activity_main), BoardView.OnMoveListener {

  private lateinit var viewModel: MainViewModel

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    initViewModel()
    setListeners()
    setupBottomSheet()
    viewModel.currentSide.observe(this, { currentSide ->
      tvSide.text = currentSide.name
    })
    viewModel.boardPieces.observe(this, { boardPieces ->
      boardView.submitBoardPieces(boardPieces)
    })
    viewModel.boardTouchEnable.observe(this, { boardTouchEnable ->
      boardView.setInteractionEnable(boardTouchEnable)
    })
    viewModel.gameResult.observe(this, { result ->
      if (result.showDialog) {
        val message = when (result.gameResult) {
          Game.GameResult.Draw -> "Draw!"
          is Game.GameResult.Winner -> "Winner: ${result.gameResult.side.name}!"
        }
        AlertDialog.Builder(this)
          .setTitle("Game has ended!")
          .setMessage(message)
          .setCancelable(true)
          .setOnCancelListener { viewModel.clearResultShowFlag() }
          .create()
          .show()
      }
    })
  }

  override fun onMove(move: Move) {
    viewModel.doMove(move)
  }

  private fun setListeners() {
    boardView.setOnMoveListener(this)
    btnNewGameBlack.setOnClickListener {
      viewModel.startNewGame(PieceSide.BLACK)
      collapseBottomSheet()
    }
    btnNewGameWhite.setOnClickListener {
      viewModel.startNewGame(PieceSide.WHITE)
      collapseBottomSheet()
    }
    btnUndo.setOnClickListener {
      viewModel.undo()
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
      application,
      MainThreadExecutor(),
      Executors.newSingleThreadExecutor(),
      mutableMapOf(),
      // todo: check for potential leak
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
      btnUndo.animate()
        .setDuration(0)
        .scaleX(alphaScale)
        .scaleY(alphaScale)
        .alpha(alphaScale)
        .start()
    }
  }

  private class MainViewModelFactory(
    private val application: Application,
    private val mainThreadExecutor: Executor,
    private val engineExecutor: Executor,
    private val bitmapCache: MutableMap<Int, Bitmap>
  ) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
      return MainViewModel(
        application,
        mainThreadExecutor,
        engineExecutor,
        BlackPiecesResourceProvider(bitmapCache),
        WhitePiecesResourceProvider(bitmapCache)
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

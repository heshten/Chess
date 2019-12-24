package com.heshten.chess.ui

import android.content.res.Resources
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.heshten.chess.R
import com.heshten.chess.core.logic.Game
import com.heshten.chess.core.logic.movecheckers.DiagonalMovesChecker
import com.heshten.chess.core.logic.movecheckers.HorizontalMovesChecker
import com.heshten.chess.core.logic.movecheckers.KnightLikeMovesChecker
import com.heshten.chess.core.logic.movecheckers.VerticalMovesChecker
import com.heshten.chess.core.models.BoardPosition
import com.heshten.chess.core.models.pieces.Piece
import com.heshten.chess.core.recources.BlackPiecesResourceProvider
import com.heshten.chess.core.recources.WhitePiecesResourceProvider
import com.heshten.chess.ui.views.listeners.OnPieceSelectListener
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), OnPieceSelectListener {

    private lateinit var game: Game

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initGame(resources)
        setListeners()
    }

    override fun onPieceSelected(piece: Piece) {
        game.pieceSelected(piece)
    }

    override fun onSelectedPositionSelected(boardPosition: BoardPosition) {
        game.selectedPositionClicked(boardPosition)
    }

    private fun setListeners() {
        boardView.setPieceSelectListener(this)
        btnNewGame.setOnClickListener { game.start() }
    }

    private fun initGame(resources: Resources) {
        //di
        val blackPiecesResourceProvider = BlackPiecesResourceProvider(resources)
        val whitePiecesResourceProvider = WhitePiecesResourceProvider(resources)
        val horizontalMovesChecker = HorizontalMovesChecker()
        val verticalMovesChecker = VerticalMovesChecker()
        val diagonalMovesChecker = DiagonalMovesChecker()
        val knightLikeMovesChecker = KnightLikeMovesChecker()
        //create instance
        game = Game(blackPiecesResourceProvider, whitePiecesResourceProvider,
            verticalMovesChecker, horizontalMovesChecker, diagonalMovesChecker,
            knightLikeMovesChecker, boardView
        )
    }

}

package com.heshten.chess.ui

import android.content.res.Resources
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.heshten.chess.R
import com.heshten.chess.core.Game
import com.heshten.chess.core.board.ChessBoard
import com.heshten.chess.core.board.NewGameBoardCreator
import com.heshten.chess.core.logic.facedes.MoveCheckerFacade
import com.heshten.chess.core.logic.facedes.TakeCheckerFacade
import com.heshten.chess.core.logic.movecheckers.DiagonalMovesChecker
import com.heshten.chess.core.logic.movecheckers.HorizontalMovesChecker
import com.heshten.chess.core.logic.movecheckers.KnightLikeMovesChecker
import com.heshten.chess.core.logic.movecheckers.VerticalMovesChecker
import com.heshten.chess.core.logic.takecheckers.DiagonalTakeChecker
import com.heshten.chess.core.logic.takecheckers.HorizontalTakeChecker
import com.heshten.chess.core.logic.takecheckers.KnightLikeTakeChecker
import com.heshten.chess.core.logic.takecheckers.VerticalTakeChecker
import com.heshten.chess.core.models.BoardPosition
import com.heshten.chess.core.models.PieceSide
import com.heshten.chess.core.models.pieces.Piece
import com.heshten.chess.core.recources.BlackPiecesResourceProvider
import com.heshten.chess.core.recources.WhitePiecesResourceProvider
import com.heshten.chess.core.validator.SideMoveValidator
import com.heshten.chess.ui.views.listeners.OnPieceSelectListener
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), OnPieceSelectListener {

    private lateinit var game: Game

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
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
        btnNewGame.setOnClickListener {
            val bottomSide = when (sideSwitch.isChecked) {
                true -> PieceSide.WHITE
                false -> PieceSide.BLACK
            }
            startNewGame(resources, bottomSide)
        }
    }

    private fun startNewGame(resources: Resources, bottomSide: PieceSide) {
        val topSide = if (bottomSide == PieceSide.WHITE) PieceSide.BLACK else PieceSide.WHITE
        //di
        val bResourceProvider = BlackPiecesResourceProvider(resources)
        val wResourceProvider = WhitePiecesResourceProvider(resources)
        val newGameBoardCreator = NewGameBoardCreator(wResourceProvider, bResourceProvider)
        val board = ChessBoard(topSide, bottomSide, newGameBoardCreator)
        val horizontalMovesChecker = HorizontalMovesChecker(board)
        val knightLikeMovesChecker = KnightLikeMovesChecker(board)
        val verticalMovesChecker = VerticalMovesChecker(board)
        val diagonalMovesChecker = DiagonalMovesChecker(board)
        val horizontalTakesChecker = HorizontalTakeChecker(board)
        val knightLikeTakesChecker = KnightLikeTakeChecker(board)
        val verticalTakesChecker = VerticalTakeChecker(board)
        val diagonalTakesChecker = DiagonalTakeChecker(board)
        val sideValidator = SideMoveValidator()
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
        //create new game instance
        game = Game(board, boardView, moveCheckerFacade, takeCheckerFacade, sideValidator)
        game.start()
    }

}

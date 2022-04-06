package com.heshten.chess.ui.views

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import androidx.core.content.ContextCompat
import com.heshten.chess.R
import com.heshten.chess.core.board.ChessBoard
import com.heshten.chess.core.models.BoardPosition
import com.heshten.chess.core.models.pieces.Piece
import com.heshten.chess.ui.views.listeners.OnPieceSelectListener

class BoardView @JvmOverloads constructor(
  context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr), View.OnTouchListener {

  companion object {
    private const val BOARD_SIZE = 8
  }

  private val boardSquareRect = Rect()
  private var chessBoard: ChessBoard? = null
  private var onPieceSelectListener: OnPieceSelectListener? = null

  private val darkSquarePaint = Paint().apply {
    color = ContextCompat.getColor(context, R.color.darkBoardSquareColor)
    style = Paint.Style.FILL
    isAntiAlias = true
  }

  private val lightSquarePaint = Paint().apply {
    color = ContextCompat.getColor(context, R.color.lightBoardSquareColor)
    style = Paint.Style.FILL
    isAntiAlias = true
  }

  private val selectedSquarePaint = Paint().apply {
    color = ContextCompat.getColor(context, R.color.selectedColor)
    style = Paint.Style.FILL
    isAntiAlias = true
  }

  private val dotPaint = Paint().apply {
    color = ContextCompat.getColor(context, R.color.selectedColor)
    style = Paint.Style.FILL
    isAntiAlias = true
  }

  init {
    setOnTouchListener(this)
  }

  override fun onTouch(v: View?, event: MotionEvent?): Boolean {
    if (event == null || event.actionMasked != MotionEvent.ACTION_UP) {
      return true
    }

    val touchedPiece = getPieceForTouchEvent(event)
    val touchedPosition = getBoardPositionForTouchEvent(event)
    if (touchedPiece != null) {
      if (isPossibleMoveTo(touchedPosition)) {
        onPieceSelectListener?.moveSelectedPieceToPosition(touchedPosition)
      } else {
        onPieceSelectListener?.onPieceSelected(touchedPiece)
      }
    } else {
      if (isPossibleMoveTo(touchedPosition)) {
        onPieceSelectListener?.moveSelectedPieceToPosition(touchedPosition)
      }
    }
    return true
  }

  override fun onDraw(canvas: Canvas?) {
    if (canvas == null) return
    (0 until BOARD_SIZE).forEach { rowIndex ->
      drawRow(canvas, rowIndex)
    }
  }

  fun redrawBoard() {
    invalidate()
  }

  fun setChessBoard(chessBoard: ChessBoard) {
    this.chessBoard = chessBoard
    redrawBoard()
  }

  fun setPieceSelectListener(listener: OnPieceSelectListener) {
    onPieceSelectListener = listener
  }

  private fun drawRow(canvas: Canvas, rowIndex: Int) {
    val isLightFirst = rowIndex % 2 == 0
    (0 until BOARD_SIZE).forEach { columnIndex ->
      val shiftIndex = if (isLightFirst) 0 else 1
      val iterationPosition = BoardPosition(rowIndex, columnIndex)
      val pieceAtIterationPosition = pieceAtPosition(iterationPosition)
      val squarePaint = if (columnIndex % 2 == shiftIndex) {
        lightSquarePaint
      } else {
        darkSquarePaint
      }

      val squareSize = width / BOARD_SIZE
      boardSquareRect.left = squareSize * columnIndex
      boardSquareRect.right = squareSize * columnIndex + squareSize
      boardSquareRect.top = squareSize * rowIndex
      boardSquareRect.bottom = squareSize * rowIndex + squareSize

      if (isPossibleMoveTo(iterationPosition)) {
        if (pieceAtIterationPosition == null) {
          //regular bg paint with dot
          drawSquare(canvas, boardSquareRect, squarePaint)
          drawDot(canvas, boardSquareRect)
        } else {
          //selected square bg
          drawSquare(canvas, boardSquareRect, selectedSquarePaint)
        }
      } else {
        //regular bg paint
        drawSquare(canvas, boardSquareRect, squarePaint)
      }

      //draw piece if any
      if (pieceAtIterationPosition != null) {
        drawPiece(canvas, pieceAtIterationPosition, boardSquareRect)
      }
    }
  }

  private fun drawSquare(canvas: Canvas, square: Rect, squarePaint: Paint) {
    canvas.drawRect(square, squarePaint)
  }

  private fun drawPiece(canvas: Canvas, piece: Piece, square: Rect) {
    canvas.drawBitmap(piece.bitmap, null, square, null)
  }

  private fun drawDot(canvas: Canvas, rect: Rect) {
    val cx = rect.centerX().toFloat()
    val cy = rect.centerY().toFloat()
    val radius = (rect.right - rect.left) / 5f
    canvas.drawCircle(cx, cy, radius, dotPaint)
  }

  private fun getBoardPositionForTouchEvent(event: MotionEvent): BoardPosition {
    val squareSize = width / BOARD_SIZE
    val rowIndex = event.y / squareSize
    val columnIndex = event.x / squareSize
    return BoardPosition(rowIndex.toInt(), columnIndex.toInt())
  }

  private fun getPieceForTouchEvent(event: MotionEvent): Piece? {
    val position = getBoardPositionForTouchEvent(event)
    return pieceAtPosition(position)
  }

  private fun isPossibleMoveTo(boardPosition: BoardPosition): Boolean {
    return chessBoard?.isPossibleMoveTo(boardPosition) ?: false
  }

  private fun pieceAtPosition(boardPosition: BoardPosition): Piece? {
    return chessBoard?.getPieceForPosition(boardPosition)
  }
}

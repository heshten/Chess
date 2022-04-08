package com.heshten.chess.ui.views

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import androidx.core.content.ContextCompat
import com.heshten.chess.R
import com.heshten.core.models.BoardPosition
import kotlin.math.min

class BoardView @JvmOverloads constructor(
  context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr), View.OnTouchListener {

  companion object {
    private const val BOARD_SIZE = 8
  }

  private val boardSquareRect = Rect()
  private var boardUnits = mapOf<BoardPosition, BoardUnit>()
  private var onPositionTouchListener: OnPositionTouchListener? = null

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

  private val highlightedSquarePaint = Paint().apply {
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
    val touchedPosition = getBoardPositionForTouchEvent(event)
    onPositionTouchListener?.onPositionTouched(touchedPosition)
    return true
  }

  override fun onDraw(canvas: Canvas?) {
    if (canvas == null) return
    (0 until BOARD_SIZE).forEach { rowIndex ->
      drawRow(canvas, rowIndex)
    }
  }

  override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
    val widthSize = MeasureSpec.getSize(widthMeasureSpec)
    val heightSize = MeasureSpec.getSize(heightMeasureSpec)
    val expectedSize = min(widthSize, heightSize)
    super.onMeasure(
      MeasureSpec.makeMeasureSpec(expectedSize, MeasureSpec.EXACTLY),
      MeasureSpec.makeMeasureSpec(expectedSize, MeasureSpec.EXACTLY)
    )
  }

  fun submitUnits(units: Map<BoardPosition, BoardUnit>) {
    boardUnits = units
    invalidate()
  }

  fun setOnPositionTouchListener(listener: OnPositionTouchListener) {
    onPositionTouchListener = listener
  }

  private fun drawRow(canvas: Canvas, rowIndex: Int) {
    val isLightFirst = rowIndex % 2 == 0
    (0 until BOARD_SIZE).forEach { columnIndex ->
      val shiftIndex = if (isLightFirst) 0 else 1
      val iterationPosition = BoardPosition(rowIndex, columnIndex)
      val unitAtIterationPosition = boardUnits.getOr(iterationPosition, BoardUnit.Empty)
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

      when (unitAtIterationPosition) {
        BoardUnit.Empty -> {
          drawSquare(canvas, boardSquareRect, squarePaint)
        }
        BoardUnit.Dot -> {
          drawSquare(canvas, boardSquareRect, squarePaint)
          drawDot(canvas, boardSquareRect)
        }
        is BoardUnit.Piece -> {
          if (unitAtIterationPosition.isHighlighted) {
            drawSquare(canvas, boardSquareRect, highlightedSquarePaint)
          } else {
            drawSquare(canvas, boardSquareRect, squarePaint)
          }
          drawPiece(canvas, unitAtIterationPosition.pieceBitmap, boardSquareRect)
        }
      }
    }
  }

  private fun drawSquare(canvas: Canvas, square: Rect, squarePaint: Paint) {
    canvas.drawRect(square, squarePaint)
  }

  private fun drawPiece(canvas: Canvas, bitmap: Bitmap, square: Rect) {
    canvas.drawBitmap(bitmap, null, square, null)
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

  private fun Map<BoardPosition, BoardUnit>.getOr(
    key: BoardPosition,
    default: BoardUnit
  ): BoardUnit {
    return if (this.containsKey(key)) {
      this.getValue(key)
    } else {
      default
    }
  }

  sealed class BoardUnit {
    object Empty : BoardUnit()
    object Dot : BoardUnit()
    data class Piece(
      val isHighlighted: Boolean,
      val pieceBitmap: Bitmap
    ) : BoardUnit()
  }

  interface OnPositionTouchListener {

    fun onPositionTouched(boardPosition: BoardPosition)
  }
}

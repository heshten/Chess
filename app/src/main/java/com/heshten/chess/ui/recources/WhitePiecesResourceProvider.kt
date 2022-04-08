package com.heshten.chess.ui.recources

import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import com.heshten.chess.R

class WhitePiecesResourceProvider(
  private val resources: Resources
) : PieceResourceProvider {

  override fun getPawnBitmap(): Bitmap = getBitmap(R.drawable.w_pawn_png_shadow)

  override fun getRookBitmap(): Bitmap = getBitmap(R.drawable.w_rook_png_shadow)

  override fun getKnightBitmap(): Bitmap = getBitmap(R.drawable.w_knight_png_shadow)

  override fun getBishopBitmap(): Bitmap = getBitmap(R.drawable.w_bishop_png_shadow)

  override fun getQueenBitmap(): Bitmap = getBitmap(R.drawable.w_queen_png_shadow)

  override fun getKingBitmap(): Bitmap = getBitmap(R.drawable.w_king_png_shadow)

  private fun getBitmap(resourceId: Int): Bitmap {
    return BitmapFactory.decodeResource(resources, resourceId)
  }
}

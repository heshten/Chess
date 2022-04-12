package com.heshten.chess.ui.recources

import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import com.heshten.chess.R

class WhitePiecesResourceProvider(
  private val cache: MutableMap<Int, Bitmap>
) : PieceResourceProvider {

  override fun getPawnBitmap(resources: Resources): Bitmap =
    getBitmap(R.drawable.w_pawn_png_shadow, resources)

  override fun getRookBitmap(resources: Resources): Bitmap =
    getBitmap(R.drawable.w_rook_png_shadow, resources)

  override fun getKnightBitmap(resources: Resources): Bitmap =
    getBitmap(R.drawable.w_knight_png_shadow, resources)

  override fun getBishopBitmap(resources: Resources): Bitmap =
    getBitmap(R.drawable.w_bishop_png_shadow, resources)

  override fun getQueenBitmap(resources: Resources): Bitmap =
    getBitmap(R.drawable.w_queen_png_shadow, resources)

  override fun getKingBitmap(resources: Resources): Bitmap =
    getBitmap(R.drawable.w_king_png_shadow, resources)

  private fun getBitmap(resourceId: Int, resources: Resources): Bitmap {
    val cachedBitmap = cache[resourceId]
    if (cachedBitmap == null) {
      cache[resourceId] =
        BitmapFactory.decodeResource(resources, resourceId)
    }
    return cache.getValue(resourceId)
  }
}

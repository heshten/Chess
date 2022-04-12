package com.heshten.chess.ui.recources

import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import com.heshten.chess.R

class BlackPiecesResourceProvider(
  private val cache: MutableMap<Int, Bitmap>
) : PieceResourceProvider {

  override fun getPawnBitmap(resources: Resources): Bitmap =
    getBitmap(R.drawable.b_pawn_png_shadow, resources)

  override fun getRookBitmap(resources: Resources): Bitmap =
    getBitmap(R.drawable.b_rook_png_shadow, resources)

  override fun getKnightBitmap(resources: Resources): Bitmap =
    getBitmap(R.drawable.b_knight_png_shadow, resources)

  override fun getBishopBitmap(resources: Resources): Bitmap =
    getBitmap(R.drawable.b_bishop_png_shadow, resources)

  override fun getQueenBitmap(resources: Resources): Bitmap =
    getBitmap(R.drawable.b_queen_png_shadow, resources)

  override fun getKingBitmap(resources: Resources): Bitmap =
    getBitmap(R.drawable.b_king_png_shadow, resources)

  private fun getBitmap(resourceId: Int, resources: Resources): Bitmap {
    val cachedBitmap = cache[resourceId]
    if (cachedBitmap == null) {
      cache[resourceId] =
        BitmapFactory.decodeResource(resources, resourceId)
    }
    return cache.getValue(resourceId)
  }
}

package com.heshten.chess.ui.recources

import android.content.res.Resources
import android.graphics.Bitmap

interface PieceResourceProvider {

  fun getPawnBitmap(resources: Resources): Bitmap

  fun getRookBitmap(resources: Resources): Bitmap

  fun getKnightBitmap(resources: Resources): Bitmap

  fun getBishopBitmap(resources: Resources): Bitmap

  fun getQueenBitmap(resources: Resources): Bitmap

  fun getKingBitmap(resources: Resources): Bitmap
}

package com.heshten.chess.core.recources

import android.graphics.Bitmap

interface PieceResourceProvider {
    fun getPawnBitmap(): Bitmap
    fun getRookBitmap(): Bitmap
    fun getKnightBitmap(): Bitmap
    fun getBishopBitmap(): Bitmap
    fun getQueenBitmap(): Bitmap
    fun getKingBitmap(): Bitmap
}
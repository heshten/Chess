package com.heshten.chess.core.recources

import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import com.heshten.chess.R

class BlackPiecesResourceProvider(private val resources: Resources) : PieceResourceProvider {

    override fun getPawnBitmap(): Bitmap = getBitmap(R.drawable.b_pawn_png_shadow)

    override fun getRookBitmap(): Bitmap = getBitmap(R.drawable.b_rook_png_shadow)

    override fun getKnightBitmap(): Bitmap = getBitmap(R.drawable.b_knight_png_shadow)

    override fun getBishopBitmap(): Bitmap = getBitmap(R.drawable.b_bishop_png_shadow)

    override fun getQueenBitmap(): Bitmap = getBitmap(R.drawable.b_queen_png_shadow)

    override fun getKingBitmap(): Bitmap = getBitmap(R.drawable.b_king_png_shadow)

    private fun getBitmap(resourceId: Int): Bitmap {
        return BitmapFactory.decodeResource(resources, resourceId)
    }

}
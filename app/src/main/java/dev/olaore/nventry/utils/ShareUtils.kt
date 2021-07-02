package dev.olaore.nventry.utils

import android.Manifest
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.net.Uri
import android.provider.MediaStore
import android.util.Log
import android.widget.ImageView
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.nio.ByteBuffer

val imagePermissions = listOf(
    Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE
)
var image: Bitmap? = null

fun shareProduct(ctx: Context, text: String, imageView: ImageView, title: String): Boolean {

    val shareIntent = Intent(Intent.ACTION_SEND).apply {
        type = "image/*"
        putExtra(Intent.EXTRA_TEXT, text)
        putExtra(Intent.EXTRA_STREAM, getImageUri(
            ctx, getBitmapFromView(imageView)
        ))
    }

    ctx.startActivity(Intent.createChooser(shareIntent, title))

    return true

}

private fun getBitmapFromView(view: ImageView): Bitmap {
    val bitmap = Bitmap.createBitmap(view.width, view.height, Bitmap.Config.ARGB_8888)
    val canvas = Canvas(bitmap)
    view.draw(canvas)
    return bitmap
}

private fun getImageUri(ctx: Context, bitmap: Bitmap): Uri? {
    val bytes = ByteArrayOutputStream()
    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes)
    val imagePath = MediaStore.Images.Media.insertImage(ctx.contentResolver, bitmap, "title", null)
    return Uri.parse(imagePath)
}
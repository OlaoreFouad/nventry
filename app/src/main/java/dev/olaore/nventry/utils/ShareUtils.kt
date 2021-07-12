package dev.olaore.nventry.utils

import android.Manifest
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.util.Log
import android.widget.ImageView
import dev.olaore.nventry.ui.receivers.SharedProductsBroadcastReceiver
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.nio.ByteBuffer

val imagePermissions = listOf(
    Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE
)
var image: Bitmap? = null

const val SHARE_PRODUCT_REQUEST_CODE = 1000

// shareProduct logic
fun shareProduct(
    ctx: Context,
    text: String,
    imageView: ImageView,
    title: String,
    imageUrl: String
): Boolean {

    // create intent to share product
    val shareIntent = Intent(Intent.ACTION_SEND).apply {
        type = "image/*"
        putExtra(Intent.EXTRA_TEXT, text)
        putExtra(
            Intent.EXTRA_STREAM, getImageUri(
                ctx, getBitmapFromView(imageView) // get image from imageView
            )
        )
    }

    // get pending intent to react post-share event
    val pendingIntent = PendingIntent.getBroadcast(
        ctx,
        SHARE_PRODUCT_REQUEST_CODE,
        Intent(ctx, SharedProductsBroadcastReceiver::class.java),
        PendingIntent.FLAG_UPDATE_CURRENT
    )

    // launch intent based on the installed android version
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1) {
        Prefs.saveProductDetails(ctx, title.substring(5, title.length), text, imageUrl)
        ctx.startActivity(Intent.createChooser(shareIntent, title, pendingIntent.intentSender))
    } else {
        ctx.startActivity(Intent.createChooser(shareIntent, title))
    }

    return true

}

// retrieve image from view
private fun getBitmapFromView(view: ImageView): Bitmap {
    val bitmap = Bitmap.createBitmap(view.width, view.height, Bitmap.Config.ARGB_8888)
    val canvas = Canvas(bitmap)
    view.draw(canvas)
    return bitmap
}

// install image temporarily and save in file storage
private fun getImageUri(ctx: Context, bitmap: Bitmap): Uri? {
    val bytes = ByteArrayOutputStream()
    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes)
    val imagePath = MediaStore.Images.Media.insertImage(ctx.contentResolver, bitmap, "title", null)
    return Uri.parse(imagePath)
}
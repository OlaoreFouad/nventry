package dev.olaore.nventry.utils

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.res.Resources
import android.net.Uri
import android.text.Spannable
import android.text.SpannableString
import android.text.Spanned
import android.text.style.ClickableSpan
import android.util.Log
import android.util.Patterns
import android.util.TypedValue
import android.view.View
import android.widget.TextView
import com.google.android.material.snackbar.Snackbar
import dev.olaore.nventry.R
import dev.olaore.nventry.models.domain.Partner
import java.util.*

class CustomClickableSpan(
    private val instruction: () -> Unit
) : ClickableSpan() {

    override fun onClick(p0: View) {
        instruction.invoke()
    }

}

fun getStubPartners(): List<Partner> {

    return listOf(
        Partner(UUID.randomUUID().toString(), "Damilola", R.drawable.avatar_1, "d@dami@gmail.com"),
        Partner(UUID.randomUUID().toString(), "Amotul-Mujeeb", R.drawable.avatar_2, "amah@gmail.com"),
        Partner(UUID.randomUUID().toString(), "Salaudeen", R.drawable.avatar_3, "salaudeen@gmail.com"),
        Partner(UUID.randomUUID().toString(), "Limzy", R.drawable.avatar_4, "limzy@gmail.com")
    )

}

fun createSpannableText(srcText: String, letter: String, textLength: Int): String {
    val textIndex = srcText.indexOf(letter)
    val textString = SpannableString(srcText)

    val clickableSpan = CustomClickableSpan {
        Log.d("Spannables", "$textString")
    }

    textString.setSpan(
        clickableSpan, textIndex, textIndex + textLength, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
    )

    return textString.toString()
}

fun isValidEmail(value: String): Boolean {
    return Patterns.EMAIL_ADDRESS.matcher(value).matches()
}

fun showSnackbar(view: View, text: String) {
    val snackbar = Snackbar.make(view, text, Snackbar.LENGTH_LONG)
    val snackbarView = snackbar.view

    val textView = snackbarView.findViewById<TextView>(com.google.android.material.R.id.snackbar_text)
    textView.maxLines = 4
    snackbar.show()
}

object Utils {

    const val BUSINESS_ID: String = "BUSINESS_ID"
    const val REQUEST_FILE_CODE = 1

}

fun Context.copyToClipboard(text: String, callback: () -> Unit) {
    val clipboard = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
    val clip = ClipData.newPlainText("label", text)
    clipboard.setPrimaryClip(clip)

    callback()
}

fun List<String>.asURIs(): List<Uri> {
    return this.map { Uri.parse(it) }
}
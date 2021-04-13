package dev.olaore.nventry.utils

import android.text.Spannable
import android.text.SpannableString
import android.text.Spanned
import android.text.style.ClickableSpan
import android.util.Log
import android.util.Patterns
import android.view.View
import android.widget.TextView
import com.google.android.material.snackbar.Snackbar

class CustomClickableSpan(
    private val instruction: () -> Unit
) : ClickableSpan() {

    override fun onClick(p0: View) {
        instruction.invoke()
    }

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

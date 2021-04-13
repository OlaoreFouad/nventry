package dev.olaore.nventry.utils

import android.text.Spannable
import android.text.SpannableString
import android.text.style.ClickableSpan
import android.util.Log
import android.view.View

class CustomClickableSpan(
    private val instruction: () -> Unit
) : ClickableSpan() {

    override fun onClick(p0: View) {
        instruction.invoke()
    }

}

fun createSpannableText(src: String, text: String): String {
    val spannable = SpannableString(src)
    val indexOfText = src.indexOf(text)
    val textLength = text.length

    spannable.setSpan(
        CustomClickableSpan {
            Log.d("LoginSignup", "Login Clicked!")
        }, indexOfText, indexOfText + textLength, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
    )

    return spannable.toString()
}
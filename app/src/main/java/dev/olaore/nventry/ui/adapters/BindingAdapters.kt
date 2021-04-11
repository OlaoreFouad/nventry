package dev.olaore.nventry.ui.adapters

import android.widget.TextView
import androidx.databinding.BindingAdapter
import dev.olaore.nventry.R

@BindingAdapter("indicatorWidth")
fun setIndicatorStyle(view: TextView, state: Boolean) {

    val layoutParams = view.layoutParams
    layoutParams.width = if (state) {
        view.resources.getDimension(R.dimen.active_indicator_width).toInt()
    } else {
        view.resources.getDimension(R.dimen.indicator_size).toInt()
    }
    view.layoutParams = layoutParams

}
package dev.olaore.nventry.ui.views

import android.content.Context
import android.util.AttributeSet
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import androidx.constraintlayout.widget.ConstraintLayout
import dev.olaore.nventry.R

class UploadImageContainer @JvmOverloads
constructor(
    private val ctx: Context,
    private val attributeSet: AttributeSet? = null,
    private val defStyleAttr: Int = 0,
    private val defStyleRes: Int = 0
) : ConstraintLayout(
    ctx, attributeSet, defStyleAttr, defStyleRes
) {

    private val uploadAction: ImageView
    private val viewAction: ImageView
    private val uploadedImage: ImageView

    init {

        val inflater = LayoutInflater.from(ctx)
        inflater.inflate(R.layout.upload_image_container, this)

        setPadding(toDP(20).toInt(), toDP(30).toInt(), toDP(20).toInt(), 0)

        uploadAction = findViewById(R.id.upload_image_action)
        viewAction = findViewById(R.id.view_image_action)
        uploadedImage = findViewById(R.id.uploaded_image)
        
        uploadedImage.visibility = View.GONE

    }

    fun toDP(value: Int): Float {
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP, value.toFloat(), ctx.resources.displayMetrics
        )
    }

}
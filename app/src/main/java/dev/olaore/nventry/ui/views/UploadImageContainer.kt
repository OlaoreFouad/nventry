package dev.olaore.nventry.ui.views

import android.content.Context
import android.net.Uri
import android.provider.MediaStore
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

    interface Listener {

        fun onRequestImageUpload()

    }

    private var viewMode: Boolean = false
        set(value) {
            field = value
            uploadedImageView.visibility = if (value) View.VISIBLE else View.GONE
            viewAction.setImageResource(
                if (value) R.drawable.ic_unview else R.drawable.ic_view
            )
        }
    private val uploadAction: ImageView
    private val viewAction: ImageView
    private val uploadedImageView: ImageView
    private lateinit var listener: Listener

    var hasUploadedImage = false

    private lateinit var currentImageUri: Uri

    init {

        val inflater = LayoutInflater.from(ctx)
        inflater.inflate(R.layout.upload_image_container, this)

        setPadding(toDP(20).toInt(), toDP(30).toInt(), toDP(20).toInt(), 0)

        uploadAction = findViewById(R.id.upload_image_action)
        viewAction = findViewById(R.id.view_image_action)
        uploadedImageView = findViewById(R.id.uploaded_image)
        
        uploadedImageView.visibility = View.GONE

        uploadAction.setOnClickListener {
            this.listener.onRequestImageUpload()
        }

        viewAction.setOnClickListener {
            viewMode = !viewMode
        }

    }

    fun addListener(listener: Listener) {
        this.listener = listener
    }

    fun getCurrentImageUri() = this.currentImageUri

    private fun populateImage() {
        uploadedImageView.setImageBitmap(
            MediaStore.Images.Media.getBitmap(ctx.contentResolver, this.currentImageUri)
        )

        this.viewMode = true
    }

    fun setImage(imageUri: Uri) {
        val columns = arrayOf(MediaStore.Images.Media.DATA)
        val cursor = ctx.contentResolver.query(
            imageUri, columns, null, null, null
        )

        cursor?.let {
            // move to the first item
            it.moveToFirst()
            // get the index for the data column on the content provider
            val dataColumnIndex = it.getColumnIndex(columns[0])
            // get the string value at the index
            val filePath = cursor.getString(dataColumnIndex)
            // close the cursor
            it.close()
            filePath?.let { _ ->
                this.hasUploadedImage = true
                this.currentImageUri = imageUri
                this.populateImage()
            }
        }

    }

    fun toDP(value: Int): Float {
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP, value.toFloat(), ctx.resources.displayMetrics
        )
    }

}
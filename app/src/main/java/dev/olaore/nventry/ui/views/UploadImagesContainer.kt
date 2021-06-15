package dev.olaore.nventry.ui.views

import android.content.Context
import android.net.Uri
import android.provider.MediaStore
import android.util.AttributeSet
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import dev.olaore.nventry.R
import dev.olaore.nventry.ui.listeners.ImageInteraction
import dev.olaore.nventry.utils.showSnackbar

class UploadImagesContainer @JvmOverloads
constructor(
    private val ctx: Context,
    private val attributeSet: AttributeSet? = null,
    private val defStyleAttr: Int = 0,
    private val defStyleRes: Int = 0
) : ConstraintLayout(ctx, attributeSet, defStyleAttr, defStyleRes), ImageInteraction {

    var editMode = false

    interface Listener {

        fun onRequestImageUpload()

    }

    private var uploadAction: ImageView
    private var viewAction: ImageView
    private var imagesList: RecyclerView

    private var viewMode: Boolean = false
        set(value) {
            field = value
            imagesList.visibility = if (value) View.VISIBLE else View.GONE
            viewAction.setImageResource(
                if (value) R.drawable.ic_unview else R.drawable.ic_view
            )
        }

    private lateinit var listener: Listener
    private lateinit var imagesAdapter: ImagesAdapter
    private var images = mutableListOf<Uri>()

    init {
        val inflater = LayoutInflater.from(ctx)
        inflater.inflate(R.layout.upload_images_container, this)

        setPadding(toDP(20).toInt(), toDP(30).toInt(), toDP(20).toInt(), 0)

        viewAction = findViewById(R.id.view_image_action)
        uploadAction = findViewById(R.id.upload_image_action)
        imagesList = findViewById(R.id.images_list)

        uploadAction.setOnClickListener {
            if (this.images.size == 3) {
                showSnackbar(it, "You cannot upload more than three images")
            } else {
                this.listener.onRequestImageUpload()
            }
        }

        viewAction.setOnClickListener {
            this.viewMode = !this.viewMode
        }

    }

    override fun onDeleteImage(index: Int) {
        this.images.removeAt(index)
        this.populateImages()
    }

    override fun onImageBound() {
        this.viewMode = true
    }

    fun addListener(listener: Listener) {
        this.listener = listener;
    }

    fun getImages(): List<Uri> {
        return this.images
    }

    fun addImage(imageUri: Uri) {
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
                this.images.add(imageUri)
                this.populateImages()
            }
        }
    }

    fun setImages(imageUrls: List<String>) {
        this.editMode = true
        this.images = imageUrls.map { Uri.parse(it) }.toMutableList()
        this.populateImages()
    }

    private fun populateImages() {
        imagesAdapter = ImagesAdapter(ctx, this.images, this, this.editMode)
        imagesList.apply {
            adapter = imagesAdapter
            layoutManager = LinearLayoutManager(ctx, LinearLayoutManager.HORIZONTAL, false)
            setHasFixedSize(true)
        }
    }

    private fun toDP(value: Int): Float {
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP, value.toFloat(), ctx.resources.displayMetrics
        )
    }

}

class ImagesAdapter(
    private var ctx: Context,
    private var images: List<Uri>,
    private var imageInteraction: ImageInteraction,
    private var editMode: Boolean
) : RecyclerView.Adapter<ImagesAdapter.ImageViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
        return ImageViewHolder(
            LayoutInflater.from(ctx).inflate(R.layout.item_image, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {

        val uri = this.images[position]

        if (editMode) {
            holder.bindAsEdit(uri.toString())
        } else {
            holder.bind(uri)
        }

    }

    override fun getItemCount() = images.size

    inner class ImageViewHolder(_v: View) : RecyclerView.ViewHolder(_v) {

        private var deleteAction: ImageView = _v.findViewById(R.id.delete_image_action)
        private var actualImage: ImageView = _v.findViewById(R.id.actual_image)

        init {
            deleteAction.setOnClickListener {
                imageInteraction.onDeleteImage(adapterPosition)
            }
        }

        fun bindAsEdit(url: String) {
            Glide.with(ctx).load(url).into(actualImage)
            imageInteraction.onImageBound()
        }

        fun bind(uri: Uri) {
            actualImage.setImageBitmap(
                MediaStore.Images.Media.getBitmap(ctx.contentResolver, uri)
            )

            imageInteraction.onImageBound()
        }

    }

}
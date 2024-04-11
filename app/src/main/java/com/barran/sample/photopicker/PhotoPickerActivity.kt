package com.barran.sample.photopicker

import android.graphics.Rect
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import android.widget.*
import android.widget.AdapterView.OnItemSelectedListener
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ItemDecoration
import com.barran.sample.R
import com.barran.sample.databinding.ActivityPhotoPickerBinding
import com.barran.sample.layoutinflater.TestFactory2Activity

private const val TAG = "photoPicker"

class PhotoPickerActivity : TestFactory2Activity() {

    private val DEFAULT_PHOTO_SIZE = 3

    private var binding: ActivityPhotoPickerBinding? = null

    private var photoAdapter: PhotoAdapter? = null

    private val maxPhotoSize by lazy {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            val max = MediaStore.getPickImagesMaxLimit()
            Log.i(TAG, "max photo limit $max")
            max
        } else {
            DEFAULT_PHOTO_SIZE
        }
    }

    private var multiPhotoSize = DEFAULT_PHOTO_SIZE

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_photo_picker)
        binding = ActivityPhotoPickerBinding.bind(findViewById(R.id.act_root))

        initView()
    }

    private fun initView() {

        val pickOne = registerForActivityResult(ActivityResultContracts.PickVisualMedia()) {
            val uri = it ?: return@registerForActivityResult
            photoAdapter?.insertPhoto(uri)
        }

        val pickMore =
            registerForActivityResult(ActivityResultContracts.PickMultipleVisualMedia(DEFAULT_PHOTO_SIZE/*maxPhotoSize*/)) {
                if (it.isEmpty()) {
                    Log.i(TAG, "choose none photo")
                } else {
                    photoAdapter?.insertPhotoList(it)
                }
            }

        binding?.apply {
            rvPhotoList.layoutManager = GridLayoutManager(this@PhotoPickerActivity, 3)
            photoAdapter = PhotoAdapter()
            rvPhotoList.adapter = photoAdapter
            rvPhotoList.addItemDecoration(object : ItemDecoration() {
                override fun getItemOffsets(
                    outRect: Rect,
                    itemPosition: Int,
                    parent: RecyclerView
                ) {
                    outRect.bottom = 10
                }
            })
            rvPhotoList.viewTreeObserver.addOnGlobalLayoutListener(object :
                ViewTreeObserver.OnGlobalLayoutListener {
                override fun onGlobalLayout() {
                    photoAdapter?.width = rvPhotoList.width / 3
                    rvPhotoList.viewTreeObserver.removeOnGlobalLayoutListener(this)
                }
            })

            btnPickOne.setOnClickListener {
                pickOne.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
            }

            btnPickMore.setOnClickListener {
                pickMore.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
            }

            val spinList  = (1..maxPhotoSize).toList()

            spinnerPhotoSize.adapter = ArrayAdapter(
                this@PhotoPickerActivity,
                android.R.layout.simple_spinner_item,
                spinList
            ).apply {
                setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            }

            spinnerPhotoSize.onItemSelectedListener = object :OnItemSelectedListener{
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    multiPhotoSize = position + 1
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {

                }

            }
            spinnerPhotoSize.setSelection(multiPhotoSize - 1)
        }
    }
}

class PhotoAdapter : RecyclerView.Adapter<PhotoViewHolder>() {

    var width: Int = ViewGroup.LayoutParams.WRAP_CONTENT

    private val photoList = mutableListOf<Uri>()

    fun insertPhoto(uri: Uri) {
        photoList.add(uri)
        notifyItemInserted(photoList.size - 1)
    }

    fun insertPhotoList(list: List<Uri>) {
        val oldSize = photoList.size
        photoList.addAll(list)
        notifyItemRangeInserted(oldSize, list.size)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PhotoViewHolder {
        val image = ImageView(parent.context)
        image.layoutParams = ViewGroup.LayoutParams(width, width)
        image.scaleType = ImageView.ScaleType.CENTER_CROP
        return PhotoViewHolder(image)
    }

    override fun onBindViewHolder(holder: PhotoViewHolder, position: Int) {
        if (position < photoList.size) {
            holder.updatePhoto(photoList[position])
        } else {
            Log.e(TAG, "error photo position $position, list size ${photoList.size}")
        }
    }

    override fun getItemCount(): Int {
        return photoList.size
    }
}

class PhotoViewHolder(image: ImageView) : RecyclerView.ViewHolder(image) {
    fun updatePhoto(uri: Uri) {
        (itemView as ImageView).setImageURI(uri)
    }
}
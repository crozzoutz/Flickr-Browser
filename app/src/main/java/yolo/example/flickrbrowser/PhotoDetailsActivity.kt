package yolo.example.flickrbrowser

import android.os.Bundle
import androidx.navigation.ui.AppBarConfiguration
import com.squareup.picasso.Picasso
import yolo.example.flickrbrowser.databinding.ActivityPhotoDetailsBinding
import yolo.example.flickrbrowser.databinding.ContentPhotoDetailsBinding

class PhotoDetailsActivity : BaseActivity() {

//    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var activityBinding: ActivityPhotoDetailsBinding
    private lateinit var contentBinding: ContentPhotoDetailsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        activityBinding = ActivityPhotoDetailsBinding.inflate(layoutInflater)
        setContentView(activityBinding.root)

        activateToolbar(true)

        contentBinding = ContentPhotoDetailsBinding.inflate(layoutInflater, activityBinding.root, true)

        val photo = intent.getSerializableExtra(PHOTO_TRANSFER) as Photo
        // for parcelable

        //val photo = intent.getParcelableExtra(PHOTO_TRANSFER)as photo

       // contentBinding.photoTitles.text = photo.title
        contentBinding.photoTitles.text=resources.getString(R.string.photo_titles_text,photo.title)
        //contentBinding.photoTags.text = photo.tags
        contentBinding.photoTags.text=resources.getString(R.string.photo_tags_text,photo.tags)
        contentBinding.photoAuthor.text = photo.author



        Picasso.get()
            .load(photo.link)
            .error(R.drawable.placeholder)
            .placeholder(R.drawable.placeholder)
            .into(contentBinding.photoImage)





        }
    }




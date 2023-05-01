package yolo.example.flickrbrowser

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso


class ImageViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    var thumbnail: ImageView = view.findViewById(R.id.thumbnail)
    var title: TextView = view.findViewById(R.id.titles)
}

class RecyclerViewAdapter(private var photoList: List<Photo>) :
    RecyclerView.Adapter<ImageViewHolder>() {
    private val TAG = "RecyclerViewAdapter"

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
        // called by the layout manager when it needs a new view
        // Log.d(TAG, "OncreateViewHolder new View requested")
        val view = LayoutInflater.from(parent.context).inflate(R.layout.browse, parent, false)
        return ImageViewHolder(view)
    }

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
// called by layout manager when it wants new data in existing view

        if (photoList.isEmpty()) {
            holder.thumbnail.setImageResource(R.drawable.placeholder)
            holder.title.setText(R.string.empty_photos)
        } else {
            val photoItem = photoList[position]
            // Log.d(TAG, "onBindViewHOlder: ${photoItem.title}--> $position")
            Picasso.get()
                .load(photoItem.image)
                .error(R.drawable.placeholder)
                .placeholder(R.drawable.placeholder)
                .into(holder.thumbnail)

            holder.title.text = photoItem.title
        }
    }


    override fun getItemCount(): Int {
        Log.d(TAG, "getItemCount called")
        return if (photoList.isNotEmpty()) photoList.size else 1

    }

    //data changed
    fun loadNewData(newPhotos: List<Photo>) {
        photoList = newPhotos
        notifyDataSetChanged()
    }

    //for enlarged photo when thumbnail clicked
    fun getPhoto(position: Int): Photo? {
        return if (photoList.isNotEmpty()) photoList[position] else null
    }
}

package yolo.example.flickrbrowser

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.preference.PreferenceManager
import androidx.recyclerview.widget.LinearLayoutManager
import yolo.example.flickrbrowser.databinding.ActivityMainBinding

class MainActivity : BaseActivity(), RawData.OnDownloadComplete,
    FlickrJsonData.OnDataAvailable,RecyclerItemClickListener.OnRecyclerClickListener {

    private val TAG = "MainActivity"

    private val recyclerViewAdapter = RecyclerViewAdapter(ArrayList())
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        Log.d(TAG, "OnCreate called")
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        activateToolbar(false)



        binding.maincontainer.recyclerview.layoutManager = LinearLayoutManager(this)
        binding.maincontainer.recyclerview.addOnItemTouchListener(RecyclerItemClickListener(this,binding.maincontainer.recyclerview,this))
        binding.maincontainer.recyclerview.adapter=recyclerViewAdapter






//

//        binding.fab.setOnClickListener { view ->
//            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                .setAction("Action", null).show()
//        }
        Log.d(TAG, "OnCreate ends")

    }

    override fun onItemClick(view: View, position: Int) {
        Log.d(TAG,"onItemClick called")
        Toast.makeText(this,"Normal tap at position $position",Toast.LENGTH_SHORT).show()
    }

    override fun onItemLongClick(view: View, position: Int) {
        Log.d(TAG,"onItemLongClick called")

        val photo = recyclerViewAdapter.getPhoto(position)
        if (photo!=null){
            val intent =Intent(this,PhotoDetailsActivity::class.java)

            intent.putExtra(PHOTO_TRANSFER,photo)
            startActivity(intent)
        }

    }

    private fun createUri(
        baseURL: String,
        searchCriteria: String,
        lang: String,
        matchALL: Boolean
    ): String {
        return Uri.parse(baseURL)
            .buildUpon()
            .appendQueryParameter("tags", searchCriteria)
            .appendQueryParameter("tag mode", if (matchALL) "ALL" else "ANY")
            .appendQueryParameter("lang", lang)
            .appendQueryParameter("format", "json")
            .appendQueryParameter("nojsoncallback", "1")
            .build()
            .toString()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        Log.d(TAG, "OnCreateOptionsMenu called")
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        Log.d(TAG, "OnOptionsItemSelected called")
        return when (item.itemId) {
            R.id.action_search -> {
                startActivity(Intent(this,SearchActivity::class.java))
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }


    //    companion object{
//        private const val  TAG = "MainActivity"
//    }
    override fun onDownloadComplete(data: String, status: DownloadStatus) {
        if (status == DownloadStatus.OK) {
            Log.d(TAG, "onDownloadComplete called ")

            val flickrJsonData = FlickrJsonData(this)
            flickrJsonData.execute(data)
        } else {
            Log.d(TAG, "onDownloadComplete failed with status $status. error message is : $data")
        }
    }

    override fun onDataAvailable(data: List<Photo>) {
        Log.d(TAG, "onDataAvailable called ")
        recyclerViewAdapter.loadNewData(data)



        Log.d(TAG, "onDataAvailable ends")
    }

    override fun onError(exception: Exception) {
        Log.e(TAG, "onError called with ${exception.message}")
    }

    override fun onResume() {
        super.onResume()

        val sharedPref= PreferenceManager.getDefaultSharedPreferences(applicationContext)
        val queryResult = sharedPref.getString(FLICKR_QUERY,"")

        if (queryResult != null) {
            if (queryResult.isNotEmpty()){
                val url = createUri("https://www.flickr.com/services/feeds/photos_public.gne", queryResult, "en-us", true)


                val rawData = RawData(this)
                //  rawData.setDownloadCompleteListener(this)
                rawData.execute(url)

            }
        }
    }
}
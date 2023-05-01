package yolo.example.flickrbrowser

import android.app.SearchManager
import android.content.Context
import android.os.Bundle
import androidx.preference.PreferenceManager
import android.util.Log
import android.view.Menu
import android.widget.SearchView
import androidx.navigation.ui.AppBarConfiguration
import yolo.example.flickrbrowser.databinding.ActivitySearchBinding


class SearchActivity : BaseActivity() {

    private val TAG= "SearchActivity"
    private var searchView : SearchView?=null

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivitySearchBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        Log.d(TAG, "base onCreate : starts")
        super.onCreate(savedInstanceState)

        binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)
        activateToolbar(true)
        Log.d(TAG, "base onCreate : ends")
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_search,menu)

        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        searchView= menu.findItem(R.id.app_bar_search).actionView as SearchView
        val searchableInfo = searchManager.getSearchableInfo(componentName)
        searchView?.setSearchableInfo(searchableInfo)

        searchView?.isIconified = false
        searchView?.setOnQueryTextListener(object :SearchView.OnQueryTextListener{


            override fun onQueryTextSubmit(query: String?): Boolean {

                val sharedPref= PreferenceManager.getDefaultSharedPreferences(applicationContext)
                sharedPref.edit().putString(FLICKR_QUERY,query).apply()
                searchView?.clearFocus()
                finish()
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }
        })


        searchView?.setOnCloseListener {
            finish()
            false
        }



        return true

    }
}



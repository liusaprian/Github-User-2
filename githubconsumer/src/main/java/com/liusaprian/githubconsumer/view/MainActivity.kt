package com.liusaprian.githubconsumer.view

import android.database.Cursor
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.loader.app.LoaderManager
import androidx.loader.content.CursorLoader
import androidx.loader.content.Loader
import androidx.recyclerview.widget.LinearLayoutManager
import com.liusaprian.githubconsumer.DatabaseContract.FavUserColumns.Companion.CONTENT_URI
import com.liusaprian.githubconsumer.DatabaseContract.FavUserColumns.Companion.getColumnNames
import com.liusaprian.githubconsumer.R
import com.liusaprian.githubconsumer.adapter.FavUserAdapter
import com.liusaprian.githubconsumer.databinding.ActivityMainBinding
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var adapter: FavUserAdapter
    private val LOADER_FAV_USER = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.favUserList.layoutManager = LinearLayoutManager(binding.favUserList.context)
        adapter = FavUserAdapter()
        binding.favUserList.adapter = adapter

        LoaderManager.getInstance(this).initLoader(LOADER_FAV_USER, null, loaderCallbacks)

        binding.swipeRefresh.setOnRefreshListener {
            refresh()
            binding.swipeRefresh.isRefreshing = false
        }

        supportActionBar?.title = getString(R.string.favorite_users)
    }

    private fun showList(isFavUserExist: Boolean) {
        binding.favUserList.visibility = if(isFavUserExist) View.VISIBLE else View.INVISIBLE
        binding.noUserFav.visibility = if(isFavUserExist) View.INVISIBLE else View.VISIBLE
    }

    private fun refresh() {
        finish()
        startActivity(intent)
        overridePendingTransition(0, 0)
    }

    private val loaderCallbacks = object : LoaderManager.LoaderCallbacks<Cursor> {
        override fun onCreateLoader(id: Int, args: Bundle?): Loader<Cursor> {
            return CursorLoader(
                applicationContext,
                CONTENT_URI,
                getColumnNames(),
                null,
                null,
                null
            )
        }

        override fun onLoadFinished(loader: Loader<Cursor>, data: Cursor?) {
            adapter.setCursor(data)
            if(adapter.itemCount == 0) showList(false)
            else showList(true)
        }

        override fun onLoaderReset(loader: Loader<Cursor>) {
            adapter.setCursor(null)
        }

    }
}
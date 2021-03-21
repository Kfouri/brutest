package com.kfouri.brutest.ui

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.kfouri.brutest.R
import com.kfouri.brutest.adapter.SearchAdapter
import com.kfouri.brutest.database.DatabaseBuilder
import com.kfouri.brutest.database.DatabaseHelper
import com.kfouri.brutest.database.DatabaseHelperImpl
import com.kfouri.brutest.model.Movie
import com.kfouri.brutest.network.ApiBuilder
import com.kfouri.brutest.network.ApiHelper
import com.kfouri.brutest.util.Status
import com.kfouri.brutest.viewmodel.SearchViewModel
import com.kfouri.brutest.viewmodel.ViewModelFactory
import kotlinx.android.synthetic.main.fragment_search.*
import kotlinx.android.synthetic.main.fragment_search.progressBar
import java.util.*

class SearchFragment: Fragment() {

    private val TAG = "SearchFragment"
    private lateinit var viewModel: SearchViewModel
    private var currentPage = 1L
    private var totalPages = 1L
    private var timer: Timer? = null

    private val dbHelper by lazy {
        activity?.applicationContext?.let {
            DatabaseBuilder.getInstance(it)
        }?.let { DatabaseHelperImpl(it) }
    }

    private val adapter by lazy {
        activity?.applicationContext?.let { SearchAdapter(it) { movie: Movie -> subscriptionClicked(movie) } }
    }

    override fun onCreateView(inflater: LayoutInflater,container: ViewGroup?,savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_search, container, false)

        viewModel = ViewModelProvider(this, ViewModelFactory(ApiHelper(ApiBuilder.apiService),dbHelper as DatabaseHelper)).get(SearchViewModel::class.java)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setRecyclerViewMovies()

        textView_cancelSearch.setOnClickListener {
            activity?.onBackPressed()
        }

        editText_search.addTextChangedListener(object: TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (timer != null) {
                    timer?.cancel()
                }
            }

            override fun afterTextChanged(editable: Editable?) {
                if (editable.toString().trim().isNotEmpty()) {
                    timer = Timer()
                    timer?.schedule(object: TimerTask() {
                        override fun run() {
                            Handler(Looper.getMainLooper()).post {
                                adapter?.clearData()
                                getMovies(editable.toString())
                            }
                        }
                    }, 800)
                } else {
                    currentPage = 1
                    totalPages = 1
                    adapter?.clearData()
                }
            }
        })

    }

    private fun getMovies(query: String) {
        viewModel.searchMovie(query, currentPage).observe(viewLifecycleOwner, {
            it?.let { resource ->
                when (resource.status) {
                    Status.SUCCESS -> {
                        recyclerView_search.visibility = View.VISIBLE
                        progressBar.visibility = View.GONE

                        resource.data?.let { data ->
                            adapter?.setData(data.results)
                            totalPages = data.totalPages
                        }

                    }
                    Status.ERROR -> {
                        recyclerView_search.visibility = View.VISIBLE
                        progressBar.visibility = View.GONE
                        Toast.makeText(activity, it.message, Toast.LENGTH_LONG).show()
                        Log.d(TAG, "Error:" + it.message)
                    }
                    Status.LOADING -> {
                        progressBar.visibility = View.VISIBLE
                        recyclerView_search.visibility = View.GONE
                    }
                }
            }
        })
    }

    private fun setRecyclerViewMovies() {
        recyclerView_search.setHasFixedSize(true)
        recyclerView_search.adapter = adapter

        recyclerView_search.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                adapter?.itemCount?.let { count ->
                    if (!recyclerView_search.canScrollVertically(1) && count > 0) {
                        if (currentPage <= totalPages) {
                            currentPage += 1
                            getMovies(editText_search.text.toString())
                        }
                    } else {
                        currentPage = 1
                        totalPages = 1
                    }
                } ?: run {
                    currentPage = 1
                    totalPages = 1
                }
            }
        })
    }

    private fun subscriptionClicked(movie: Movie) {
        viewModel.updateSubscription(movie)
    }

}
package com.kfouri.brutest.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.kfouri.brutest.R
import com.kfouri.brutest.adapter.MoviesAdapter
import com.kfouri.brutest.adapter.SubscriptionAdapter
import com.kfouri.brutest.database.DatabaseBuilder
import com.kfouri.brutest.database.DatabaseHelper
import com.kfouri.brutest.database.DatabaseHelperImpl
import com.kfouri.brutest.database.model.Subscription
import com.kfouri.brutest.model.Genres
import com.kfouri.brutest.model.Movie
import com.kfouri.brutest.network.ApiBuilder
import com.kfouri.brutest.network.ApiHelper
import com.kfouri.brutest.util.Status
import com.kfouri.brutest.viewmodel.ListViewModel
import com.kfouri.brutest.viewmodel.ViewModelFactory
import kotlinx.android.synthetic.main.fragment_list.*

class ListFragment : Fragment() {

    private val TAG = "ListFragment"
    private lateinit var viewModel: ListViewModel
    private var currentPage = 1L
    private var totalPages = 1L

    private val adapter by lazy {
        activity?.applicationContext?.let { MoviesAdapter(it) { idMovie: Long -> itemClicked(idMovie) } }
    }

    private val adapterSubscription by lazy {
        activity?.applicationContext?.let { SubscriptionAdapter(it) { idSubscription: Long -> itemClicked(idSubscription) } }
    }

    private val dbHelper by lazy {
        activity?.applicationContext?.let {
            DatabaseBuilder.getInstance(
                it
            )
        }?.let { DatabaseHelperImpl(it) }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Log.d("Kafu", "onCreateView()")
        val view = inflater.inflate(R.layout.fragment_list, container, false)
        viewModel = ViewModelProvider(
            this, ViewModelFactory(
                ApiHelper(ApiBuilder.apiService),
                dbHelper as DatabaseHelper
            )
        ).get(ListViewModel::class.java)
        viewModel.onSubscriptionsList().observe(viewLifecycleOwner, Observer { subscriptionsList ->
            manageSubscriptionList(
                subscriptionsList
            )
        })
        viewModel.getAllSubscriptions()

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d("Kafu", "onViewCreated()")
        setRecyclerViewMovies()
        setRecyclerViewSubscriptions()

        (requireActivity() as MainActivity).supportActionBar!!.show()

        getGenres()

        val onBackPressedCallback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                activity?.finish()
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner,
            onBackPressedCallback
        )
    }

    private fun getGenres() {
        viewModel.getGenres().observe(viewLifecycleOwner, Observer {

            it?.let { resource ->
                when (resource.status) {
                    Status.LOADING -> {
                    }
                    Status.SUCCESS -> {
                        resource.data?.let { it ->
                            getMovies()
                            setGenresData(it.genres)
                        }
                    }
                    Status.ERROR -> {
                        Toast.makeText(activity, it.message, Toast.LENGTH_LONG).show()
                        Log.d(TAG, "Error Genres:" + it.message)
                    }
                }
            }
        })
    }

    private fun getMovies() {
        viewModel.getMovies(currentPage).observe(viewLifecycleOwner, Observer {

            it?.let { resource ->
                when (resource.status) {
                    Status.SUCCESS -> {
                        recyclerView_movies.visibility = View.VISIBLE
                        progressBar.visibility = View.GONE

                        resource.data?.let { it ->
                            setData(it.results)
                            totalPages = it.totalPages
                        }
                    }
                    Status.ERROR -> {
                        recyclerView_movies.visibility = View.VISIBLE
                        progressBar.visibility = View.GONE
                        Toast.makeText(activity, it.message, Toast.LENGTH_LONG).show()
                        Log.d(TAG, "Error:" + it.message)
                    }
                    Status.LOADING -> {
                        progressBar.visibility = View.VISIBLE
                        recyclerView_movies.visibility = View.GONE
                    }
                }
            }
        })
    }

    private fun setRecyclerViewSubscriptions() {
        val mPopularLayoutManager: RecyclerView.LayoutManager =
            LinearLayoutManager(activity?.applicationContext, LinearLayoutManager.HORIZONTAL, false)
        recyclerView_subscription.layoutManager = mPopularLayoutManager
        recyclerView_subscription.adapter = adapterSubscription
    }

    private fun manageSubscriptionList(subscriptionsList: ArrayList<Subscription>) {

        if (subscriptionsList.size > 0) {
            adapterSubscription?.setData(subscriptionsList)
            textView_subscription.visibility = View.VISIBLE
            recyclerView_subscription.visibility = View.VISIBLE
        } else {
            textView_subscription.visibility = View.GONE
            recyclerView_subscription.visibility = View.GONE
        }
    }

    private fun setData(list: ArrayList<Movie>) {
        adapter?.setData(list)
    }

    private fun setGenresData(list: ArrayList<Genres>) {
        adapter?.setGenresData(list)
    }

    private fun setRecyclerViewMovies() {
        recyclerView_movies.setHasFixedSize(true)
        recyclerView_movies.adapter = adapter
        recyclerView_movies.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (!recyclerView_movies.canScrollVertically(1)) {
                    if (currentPage <= totalPages) {
                        currentPage += 1
                        getMovies()
                    }
                }
            }
        })
    }

    private fun itemClicked(id: Long) {
        val action = ListFragmentDirections.actionOpenDetail(id)
        findNavController().navigate(action)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("Kafu", "onCreate()")
    }

    override fun onResume() {
        super.onResume()
        Log.d("Kafu", "onResume()")
    }

    override fun onStart() {
        super.onStart()
        Log.d("Kafu", "onStart()")
    }
}
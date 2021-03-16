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
import com.kfouri.brutest.R
import com.kfouri.brutest.adapter.MoviesAdapter
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

    private val adapter by lazy {
        activity?.applicationContext?.let { MoviesAdapter(it) { movie: Movie -> itemClicked(movie) } }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setRecyclerView()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val view = inflater.inflate(R.layout.fragment_list, container, false)
        viewModel = ViewModelProvider(this, ViewModelFactory(ApiHelper(ApiBuilder.apiService))).get(ListViewModel::class.java)
        viewModel.getGenres().observe(viewLifecycleOwner, Observer {

            it?.let { resource ->
                when (resource.status) {
                    Status.SUCCESS -> {
                        resource.data?.let {
                                it -> setGenresData(it.genres)
                        }
                    }
                    Status.ERROR -> {
                        Toast.makeText(activity, it.message, Toast.LENGTH_LONG).show()
                        Log.d(TAG, "Error Genres:" + it.message)
                    }
                }
            }
        })

        viewModel.getMovies().observe(viewLifecycleOwner, Observer {

            it?.let { resource ->
                when (resource.status) {
                    Status.SUCCESS -> {
                        recyclerView_movies.visibility = View.VISIBLE
                        progressBar.visibility = View.GONE

                        resource.data?.let {
                                it -> setData(it.results)
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

        val onBackPressedCallback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                activity?.finish()
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, onBackPressedCallback)

        return view
    }

    private fun setData(list: ArrayList<Movie>) {
        adapter?.setData(list)
    }

    private fun setGenresData(list: ArrayList<Genres>) {
        adapter?.setGenresData(list)
    }

    private fun setRecyclerView() {
        recyclerView_movies.setHasFixedSize(true)
        recyclerView_movies.adapter = adapter
    }

    private fun itemClicked(movie: Movie) {
        val action = ListFragmentDirections.actionOpenDetail(movie.id)
        findNavController().navigate(action)
    }
}
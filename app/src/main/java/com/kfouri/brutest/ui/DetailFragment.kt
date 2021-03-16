package com.kfouri.brutest.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import com.kfouri.brutest.R
import com.kfouri.brutest.model.DetailResponse
import com.kfouri.brutest.network.ApiBuilder
import com.kfouri.brutest.network.ApiHelper
import com.kfouri.brutest.util.Status
import com.kfouri.brutest.viewmodel.DetailViewModel
import com.kfouri.brutest.viewmodel.ViewModelFactory
import kotlinx.android.synthetic.main.fragment_detail.*

class DetailFragment: Fragment() {

    private val TAG = "DetailFragment"
    private val args: DetailFragmentArgs by navArgs()
    private var movieId = 0
    private lateinit var viewModel: DetailViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val view = inflater.inflate(R.layout.fragment_detail, container, false)
        viewModel = ViewModelProvider(this, ViewModelFactory(ApiHelper(ApiBuilder.apiService))).get(
            DetailViewModel::class.java)

        movieId = args.ID.toInt()

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        Log.d(TAG, "onViewCreated()")

        viewModel.getMovieDetail(movieId).observe(viewLifecycleOwner, Observer {
            it?.let { resource ->
                when (resource.status) {
                    Status.SUCCESS -> {
                        progressBar.visibility = View.GONE
                        resource.data?.let { data -> showDetail(data) }
                    }
                    Status.ERROR -> {
                        progressBar.visibility = View.GONE
                        Toast.makeText(activity, it.message, Toast.LENGTH_LONG).show()
                        Log.d(TAG, "Error: $it.message")
                    }
                    Status.LOADING -> {
                        progressBar.visibility = View.VISIBLE
                    }
                }
            }
        })
    }

    private fun showDetail(data: DetailResponse) {
        textView_title.text = data.title
        textView_releaseDate.text = data.releaseDate
        textView_detailOverview.text = data.overview
    }
}
package com.kfouri.brutest.ui

import android.graphics.Color
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import androidx.palette.graphics.Palette
import androidx.room.Database
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.load.engine.Resource
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.kfouri.brutest.R
import com.kfouri.brutest.adapter.MoviesAdapter
import com.kfouri.brutest.database.DatabaseBuilder
import com.kfouri.brutest.database.DatabaseHelper
import com.kfouri.brutest.database.DatabaseHelperImpl
import com.kfouri.brutest.model.DetailResponse
import com.kfouri.brutest.model.Movie
import com.kfouri.brutest.network.ApiBuilder
import com.kfouri.brutest.network.ApiHelper
import com.kfouri.brutest.util.IMAGES_URL
import com.kfouri.brutest.util.Status
import com.kfouri.brutest.viewmodel.DetailViewModel
import com.kfouri.brutest.viewmodel.ViewModelFactory
import kotlinx.android.synthetic.main.fragment_detail.*


class DetailFragment: Fragment() {

    private val TAG = "DetailFragment"
    private val args: DetailFragmentArgs by navArgs()
    private var movieId = 0L
    private lateinit var viewModel: DetailViewModel
    private var mIsSubscribed = false
    private lateinit var mMoviePoster: String

    private val dbHelper by lazy {
        activity?.applicationContext?.let {
            DatabaseBuilder.getInstance(
                it
            )
        }?.let { DatabaseHelperImpl(it)}
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_detail, container, false)
        viewModel = ViewModelProvider(this, ViewModelFactory(ApiHelper(ApiBuilder.apiService), dbHelper as DatabaseHelper)).get(
            DetailViewModel::class.java
        )

        movieId = args.ID

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        Log.d(TAG, "onViewCreated()")

        (requireActivity() as MainActivity).supportActionBar!!.hide()

        viewModel.onSubscribed().observe(viewLifecycleOwner, Observer { isSubscribed -> updateButton(isSubscribed) })

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

        imageView_backButton.setOnClickListener {
            activity?.onBackPressed();
        }

        button_subscription.setOnClickListener {
            viewModel.updateSubscription(movieId, !mIsSubscribed, mMoviePoster)
        }
    }

    private fun showDetail(data: DetailResponse) {
        textView_title.text = data.title

        var dominatorColor: Int

        viewModel.isSubscribed(movieId)

        mMoviePoster = IMAGES_URL + data.posterPath

        activity?.applicationContext?.let {
            Glide.with(it)
                .load(IMAGES_URL + data.posterPath)
                .listener(object : RequestListener<Drawable> {
                    override fun onLoadFailed(
                        e: GlideException?,
                        model: Any?,
                        target: com.bumptech.glide.request.target.Target<Drawable>?,
                        isFirstResource: Boolean
                    ): Boolean {
                        return false
                    }

                    override fun onResourceReady(
                        resource: Drawable?,
                        model: Any?,
                        target: Target<Drawable>?,
                        dataSource: DataSource?,
                        isFirstResource: Boolean
                    ): Boolean {
                        Palette.Builder((resource as BitmapDrawable).bitmap).generate {pal ->
                            pal?.let { palette ->
                                dominatorColor = palette.getDominantColor(
                                    ContextCompat.getColor(context!!,R.color.white)
                                )
                            }
                        }
                        return false
                    }
                })
                .placeholder(R.drawable.loading_image)
                .error(R.drawable.damaged_image)
                .centerCrop()
                .into(imageView_poster)

            Glide.with(it)
                .load(IMAGES_URL + data.backdropPath)
                .listener(object : RequestListener<Drawable> {
                    override fun onLoadFailed(
                        e: GlideException?,
                        model: Any?,
                        target: com.bumptech.glide.request.target.Target<Drawable>?,
                        isFirstResource: Boolean
                    ): Boolean {
                        return false
                    }

                    override fun onResourceReady(
                        resource: Drawable?,
                        model: Any?,
                        target: Target<Drawable>?,
                        dataSource: DataSource?,
                        isFirstResource: Boolean
                    ): Boolean {
                        Palette.Builder((resource as BitmapDrawable).bitmap).generate {pal ->
                            pal?.let { palette ->
                                dominatorColor = palette.getDominantColor(
                                    ContextCompat.getColor(context!!,R.color.white)
                                )
                                viewColor.setBackgroundColor(dominatorColor);
                            }
                        }
                        return false
                    }
                })
                .centerCrop()
                .into(imageView_backPoster)
        }

        textView_releaseDate.text = data.releaseDate
        textView_detailOverview.text = data.overview
    }

    private fun updateButton(isSubscribed: Boolean) {
        mIsSubscribed = isSubscribed
        if (isSubscribed) {
            button_subscription.text = "SUSCRIPTO"
            button_subscription.background = resources.getDrawable(R.drawable.background_button_subscribed, null)
        } else {
            button_subscription.text = "SUSCRIBEME"
            button_subscription.background = resources.getDrawable(R.drawable.background_button, null)
        }


    }
}
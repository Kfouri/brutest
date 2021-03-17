package com.kfouri.brutest.ui

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
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.kfouri.brutest.R
import com.kfouri.brutest.model.DetailResponse
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
    private var movieId = 0
    private lateinit var viewModel: DetailViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_detail, container, false)
        viewModel = ViewModelProvider(this, ViewModelFactory(ApiHelper(ApiBuilder.apiService))).get(
            DetailViewModel::class.java
        )

        movieId = args.ID.toInt()

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        Log.d(TAG, "onViewCreated()")

        (requireActivity() as MainActivity).supportActionBar!!.hide()

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

    }

    private fun showDetail(data: DetailResponse) {
        textView_title.text = data.title

        var dominatorColor = 0

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
                                Log.d("Kafu", "Color: "+dominatorColor)
                                //mainContainer.setBackgroundColor(dominantColor)
                                //setColorFilter(ContextCompat.getColor(context, R.color.yourcolor), android.graphics.PorterDuff.Mode.MULTIPLY);
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
                                //mainContainer.setBackgroundColor(dominantColor)
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
}
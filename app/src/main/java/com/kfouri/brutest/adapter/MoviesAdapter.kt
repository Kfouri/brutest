package com.kfouri.brutest.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.kfouri.brutest.R
import com.kfouri.brutest.model.Genres
import com.kfouri.brutest.model.Movie
import com.kfouri.brutest.util.IMAGES_URL
import kotlinx.android.synthetic.main.movie_item.view.*

class MoviesAdapter(val context: Context, private val clickListener: (Long) -> Unit) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var list = ArrayList<Movie>()
    private var genresList = ArrayList<Genres>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return MovieViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.movie_item, parent, false))
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = list[position]
        (holder as MovieViewHolder).bind(genresList, item, clickListener, context)
        holder.itemView.item_parent.animation = AnimationUtils.loadAnimation(holder.itemView.context, R.anim.list_item_animation)
    }

    fun clearList() {
        list.clear()
    }

    fun setData(newList: ArrayList<Movie>) {
        val oldCount = list.size
        list.addAll(newList)
        notifyItemRangeInserted(oldCount, list.size)
    }

    fun setGenresData(newList: ArrayList<Genres>) {
        genresList.clear()
        genresList.addAll(newList)
    }

    class MovieViewHolder (view: View) : RecyclerView.ViewHolder(view) {
        fun bind(genresList: ArrayList<Genres>, movie: Movie, clickListener: (Long) -> Unit, context: Context){
            Glide.with(context)
                .load(IMAGES_URL + movie.posterPath)
                .error(R.drawable.damaged_image)
                .centerCrop()
                .into(itemView.imageView_poster)

            itemView.textView_title.text = movie.title
            genresList.forEach {
                if (it.id == movie.genreIds[0]) {
                    itemView.textView_category.text = it.name
                }
            }
            itemView.setOnClickListener { clickListener(movie.id) }
        }
    }

}
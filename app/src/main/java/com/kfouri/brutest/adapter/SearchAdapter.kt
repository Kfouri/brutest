package com.kfouri.brutest.adapter

import kotlinx.android.synthetic.main.search_item.view.*

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.lifecycle.viewModelScope
import androidx.navigation.Navigation
import androidx.navigation.fragment.NavHostFragment.findNavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.kfouri.brutest.R
import com.kfouri.brutest.database.DatabaseHelper
import com.kfouri.brutest.database.model.Subscription
import com.kfouri.brutest.model.Movie
import com.kfouri.brutest.ui.SearchFragment
import com.kfouri.brutest.ui.SearchFragmentDirections
import com.kfouri.brutest.util.IMAGES_URL
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.lang.Exception


class SearchAdapter(val context: Context, private val clickListener: (Movie) -> Unit) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var list = ArrayList<Movie>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return MovieViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.search_item, parent, false))
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = list[position]
        (holder as MovieViewHolder).bind(item, clickListener, context)
        holder.itemView.item_parent.animation = AnimationUtils.loadAnimation(holder.itemView.context, R.anim.list_item_animation)
    }

    fun setData(newList: ArrayList<Movie>) {
        val oldCount = list.size
        list.addAll(newList)
        notifyItemRangeInserted(oldCount, list.size)
    }

    fun clearData() {
        list.clear()
        notifyDataSetChanged()
    }

    class MovieViewHolder (view: View) : RecyclerView.ViewHolder(view) {
        fun bind(movie: Movie, clickListener: (Movie) -> Unit, context: Context){
            Glide.with(context)
                    .load(IMAGES_URL + movie.posterPath)
                    .error(R.drawable.damaged_image)
                    .centerCrop()
                    .into(itemView.imageView_posterSearch)

            itemView.textView_titleSearch.text = movie.title
            itemView.button_subscriptionSearch.setOnClickListener {
                clickListener(movie)
                movie.subscribed = !movie.subscribed
                itemView.button_subscriptionSearch.text = if (movie.subscribed) "Agregado" else "Agregar"
            }
            itemView.button_subscriptionSearch.text = if (movie.subscribed) "Agregado" else "Agregar"
            itemView.setOnClickListener {
                val action = SearchFragmentDirections.actionSearchFragmentToDetailFragment(movie.id)
                Navigation.findNavController(itemView).navigate(action)
            }
        }
    }

}
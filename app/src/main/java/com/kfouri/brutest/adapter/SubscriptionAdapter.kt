package com.kfouri.brutest.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.kfouri.brutest.R
import com.kfouri.brutest.database.model.Subscription
import kotlinx.android.synthetic.main.subscription_item.view.*

class SubscriptionAdapter(val context: Context, private val clickListener: (Long) -> Unit) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var list = ArrayList<Subscription>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return MovieViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.subscription_item, parent, false))
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = list[position]
        (holder as MovieViewHolder).bind(item, clickListener, context)
        holder.itemView.subscription_content.animation = AnimationUtils.loadAnimation(holder.itemView.context, R.anim.list_item_animation)
    }

    fun setData(newList: ArrayList<Subscription>) {
        list.clear()
        list.addAll(newList)
        notifyDataSetChanged()
    }

    class MovieViewHolder (view: View) : RecyclerView.ViewHolder(view) {
        fun bind(subscription: Subscription, clickListener: (Long) -> Unit, context: Context){
            Glide.with(context)
                .load(subscription.poster)
                .error(R.drawable.damaged_image)
                .centerCrop()
                .into(itemView.imageView_susbcriptionPoster)

            itemView.setOnClickListener { clickListener(subscription.id) }
        }
    }

}
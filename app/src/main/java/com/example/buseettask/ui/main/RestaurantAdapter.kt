package com.example.buseettask.ui.main

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.buseettask.R
import com.example.buseettask.network.Venues
import kotlinx.android.synthetic.main.restaurant_item.view.*

class RestaurantAdapter(private val clickListener: ClickListener,val context: Context, var restaurantList: List<Venues>) :
    RecyclerView.Adapter<RestaurantAdapter.RestaurantAdapterViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RestaurantAdapterViewHolder {


        val v = LayoutInflater.from(context).inflate(R.layout.restaurant_item, parent, false)
        return RestaurantAdapterViewHolder(v)
    }

    override fun onBindViewHolder(holder: RestaurantAdapterViewHolder, position: Int) {
        val restaurantItem = restaurantList[position]
        val context = holder.itemView.context
        holder.restaurantName.text = restaurantItem!!.name
        holder.address.text = restaurantItem.location.address
        holder.restaurantDirection.setOnClickListener {
            holder.itemView.context.navigateToGoogleMap(
                latitude = restaurantItem.location.lat,
                longitude = restaurantItem.location.lng
            )
        }
    }

    inner class RestaurantAdapterViewHolder internal constructor(itemView: View) :
        RecyclerView.ViewHolder(itemView) {
        internal var restaurantName: TextView = itemView.TV_retaurantname
        internal var address: TextView = itemView.TV_address
        internal var restaurantDirection: ImageView = itemView.IV_direction
    }

    interface ClickListener {
        fun onItemClick(item: Venues)
    }

    override fun getItemCount(): Int {
       return restaurantList.size
    }
     fun submitList(restaurantList: List<Venues>)
    {
        this.restaurantList=restaurantList
        notifyDataSetChanged()
    }

}

fun Context.navigateToGoogleMap(latitude: Double, longitude: Double) {
    val uri = "google.navigation:q= $latitude,$longitude"
    val intent = Intent(Intent.ACTION_VIEW)
    intent.data = Uri.parse(uri)
    this.startActivity(intent)
}

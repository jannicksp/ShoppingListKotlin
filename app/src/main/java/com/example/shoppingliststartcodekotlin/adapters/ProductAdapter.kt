package com.example.shoppingliststartcodekotlin.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.shoppingliststartcodekotlin.R
import com.example.shoppingliststartcodekotlin.data.Product
import com.example.shoppingliststartcodekotlin.data.Repository
import com.google.android.material.snackbar.Snackbar


class ProductAdapter(var products: MutableList<Product>) :
    RecyclerView.Adapter<ProductAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductAdapter.ViewHolder {
        val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.product_view, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ProductAdapter.ViewHolder, position: Int) {
        holder.itemTitle.text = products[position].name
        holder.itemPrice.text = products[position].price
        holder.itemQuantity.text = products[position].quantity
    }

    override fun getItemCount(): Int {
        return products.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        //here you need to to do stuff also - to back to the exercises
        //about recyclerviews and you can use approach that were used
        //in the exercise about recyclerviews from the book (lesson 3)
        //if you did not do that exercise - then first do that exercise in
        //a seperate project
       //  var itemImage: ImageView
        var itemTitle: TextView
        var itemPrice: TextView
        var itemQuantity: TextView
        var itemDelete: ImageButton
        var itemImage: ImageView


        init {
            itemTitle = itemView.findViewById(R.id.item_title)
            itemPrice = itemView.findViewById(R.id.item_price)
            itemQuantity = itemView.findViewById(R.id.item_quantity)
            itemDelete= itemView.findViewById(R.id.item_delete)
            itemImage= itemView.findViewById(R.id.item_image)


            itemDelete.setOnClickListener { v: View ->
                val position = adapterPosition
                Repository.deleteProduct(position)
                notifyItemRemoved(position) //this line notify the adapter
            }
        }

    }

}

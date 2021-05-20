package com.example.shoppingliststartcodekotlin.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.example.shoppingliststartcodekotlin.R
import com.example.shoppingliststartcodekotlin.data.Product
import com.example.shoppingliststartcodekotlin.data.Repository
import com.example.shoppingliststartcodekotlin.data.Repository.decrementByOne
import com.example.shoppingliststartcodekotlin.data.Repository.incrementByOne
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
        holder.itemQuantity.text = products[position].quantity.toString()
    }

    override fun getItemCount(): Int {
        return products.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
       //  var itemImage: ImageView
        var itemTitle: TextView
        var itemPrice: TextView
        var itemQuantity: TextView
        var itemDelete: ImageButton
        var itemIncrease: ImageButton
        var itemDecrease: ImageButton


        init {
            itemTitle = itemView.findViewById(R.id.item_title)
            itemPrice = itemView.findViewById(R.id.item_price)
            itemQuantity = itemView.findViewById(R.id.item_quantity)
            itemDelete= itemView.findViewById(R.id.item_delete)
            itemIncrease= itemView.findViewById(R.id.item_increase)
            itemDecrease= itemView.findViewById(R.id.item_decrease)



            itemDelete.setOnClickListener { v: View ->
                val position = adapterPosition
                val parent = v.rootView.findViewById<View>(R.id.mainView)
                val savedProduct = Repository.products[position]
                Repository.deleteProduct(position)
                notifyItemRemoved(position) //this line notify the adapter
                // Snackbar
                val snackbar = Snackbar
                    .make(parent, "Item deleted", Snackbar.LENGTH_LONG)
                    .setAction("UNDO") {
                        //This code will ONLY be executed in case that
                        //the user has hit the UNDO button
                        Repository.addProduct(savedProduct)
                        val snackbar = Snackbar.make(parent, "Item restored!", Snackbar.LENGTH_SHORT)

                        snackbar.show()
                    }

                snackbar.show()


            }

            itemIncrease.setOnClickListener { v: View ->
                val position = adapterPosition
                    incrementByOne(position)
                    notifyDataSetChanged()
            }

            itemDecrease.setOnClickListener { v: View ->
                val position = adapterPosition
                val savedProduct = Repository.products[position]
                if(savedProduct.quantity>1) {

                    decrementByOne(position)
                    notifyDataSetChanged()
                }
            }
        }

    }

}

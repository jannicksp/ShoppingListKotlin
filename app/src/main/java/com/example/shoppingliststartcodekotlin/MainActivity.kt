package com.example.shoppingliststartcodekotlin

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.shoppingliststartcodekotlin.adapters.ProductAdapter
import com.example.shoppingliststartcodekotlin.data.Product
import com.example.shoppingliststartcodekotlin.data.Repository
import com.example.shoppingliststartcodekotlin.data.Repository.addProduct
import com.google.firebase.FirebaseApp
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    //you need to have an Adapter for the products
   lateinit var adapter: ProductAdapter


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }
    private fun addNewProduct(){
        val newProduct = Product(
                name = editTextTitle.text.toString(),
                quantity = editTextQuanity.text.toString(),
                price = editTextPrice.text.toString(),
                )
        addProduct(newProduct)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        FirebaseApp.initializeApp(applicationContext)
        button_add.setOnClickListener{ addNewProduct()}

        Repository.getData().observe(this, Observer {
            Log.d("Products","Found ${it.size} products")
            updateUI()
        })
    }


    fun updateUI() {
        val layoutManager = LinearLayoutManager(this)

        /*you need to have a defined a recylerView in your
        xml file - in this case the id of the recyclerview should
        be "recyclerView" - as the code line below uses that */

       recyclerView.layoutManager = layoutManager

       adapter = ProductAdapter(Repository.products)

      /*connecting the recyclerview to the adapter  */
        recyclerView.adapter = adapter

    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        Log.d("icon_pressed", "${item.itemId}")
        when (item.itemId) {
            R.id.item_about -> {
                Toast.makeText(this, "About item clicked!", Toast.LENGTH_LONG)
                    .show()
                return true
            }
            R.id.item_delete -> {
                Toast.makeText(this, "Delete item clicked!", Toast.LENGTH_LONG)
                    .show()
                Repository.deleteAllProducts()
                return true
            }
            R.id.item_help -> {
                Toast.makeText(this, "Help item clicked!", Toast.LENGTH_LONG)
                    .show()
                return true
            }
            R.id.item_refresh -> {
                Toast.makeText(this, "Refresh item clicked!", Toast.LENGTH_LONG)
                    .show()
                return true
            }
        }

        return false //we did not handle the event

    }
}





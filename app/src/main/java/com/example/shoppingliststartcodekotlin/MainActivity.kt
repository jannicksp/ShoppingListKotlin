package com.example.shoppingliststartcodekotlin

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.shoppingliststartcodekotlin.adapters.ProductAdapter
import com.example.shoppingliststartcodekotlin.data.Product
import com.example.shoppingliststartcodekotlin.data.Repository
import com.example.shoppingliststartcodekotlin.data.Repository.addProduct
import com.google.firebase.FirebaseApp
import kotlinx.android.synthetic.main.activity_main.*
import org.pondar.dialogfragmentdemokotlinnew.MyDialogFragment

class MainActivity : AppCompatActivity() {

    //you need to have an Adapter for the products
    lateinit var adapter: ProductAdapter
    private val RESULT_CODE_PREFERENCES = 1

    fun convertListToString(): String
    {
        var result = ""
        for (product in Repository.products)
        {
            result = result + product.toString()
        }
        return result
    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }
    private fun addNewProduct(){
        val newProduct = Product(
                name = editTextTitle.text.toString(),
                quantity = editTextQuanity.text.toString().toInt(),
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
        sortNameButton.setOnClickListener {
            Repository.products.sortBy { it.name }
            adapter.notifyDataSetChanged()
        }

        sortQuantityButton.setOnClickListener {
            Repository.products.sortByDescending { it.quantity }
            adapter.notifyDataSetChanged()
        }

        //read the values at app startup so we can show this in the UI
        val name = PreferenceHandler.getName(this)
        val notifications = PreferenceHandler.useNotifications(this)
        updateUISettings(name,notifications)
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

    fun updateUISettings(name: String, notifications:Boolean){
        myName.text = name
        if (notifications)
            useNotifications.text = getString(R.string.on)
        else
            useNotifications.text = getString(R.string.off)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == RESULT_CODE_PREFERENCES)
        //the code means we came back from settings
        {
            //I can can these methods like this, because they are static
            val name = PreferenceHandler.getName(this)
            val notifications = PreferenceHandler.useNotifications(this)
            val message = "Welcome, $name"
            val toast = Toast.makeText(this, message, Toast.LENGTH_LONG)
            toast.show()
            updateUISettings(name,notifications)
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    //callback function from yes/no dialog - for yes choice
    fun positiveClicked() {
        val toast = Toast.makeText(
            this,
            "positive button clicked", Toast.LENGTH_LONG
        )
        toast.show()
        Repository.deleteAllProducts()
    }


    //callback function from yes/no dialog - for no choice
    fun negativeClick() {
        //Here we override the method and can now do something
        val toast = Toast.makeText(
            this,
            "negative button clicked", Toast.LENGTH_LONG
        )
        toast.show()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        Log.d("icon_pressed", "${item.itemId}")
        when (item.itemId) {
            R.id.item_share -> {
                /* Share content */
                val text = convertListToString() //from EditText
                val sharingIntent = Intent(Intent.ACTION_SEND)
                sharingIntent.type = "text/plain" //MIME-TYPE
                sharingIntent.putExtra(Intent.EXTRA_SUBJECT, "Shared Data")
                sharingIntent.putExtra(Intent.EXTRA_TEXT, text)
                startActivity(Intent.createChooser(sharingIntent, "Share Using"))
                return true
            }
            R.id.item_delete -> {
                Toast.makeText(this, "Delete item clicked!", Toast.LENGTH_LONG)
                    .show()
                val dialog = MyDialogFragment(::positiveClicked, ::negativeClick)
                dialog.show(supportFragmentManager, "myFragment")

                return true
            }

            R.id.item_help -> {
                Toast.makeText(this, "Help item clicked!", Toast.LENGTH_LONG)
                    .show()
                return true
            }
            R.id.item_settings -> {
                val intent = Intent(this, SettingsActivity::class.java)
                startActivityForResult(intent, RESULT_CODE_PREFERENCES)
                return true
            }

        }

        return false //we did not handle the event

    }
}





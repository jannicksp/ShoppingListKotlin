package com.example.shoppingliststartcodekotlin.data

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

object Repository {
    var products = mutableListOf<Product>()
    private lateinit var db: FirebaseFirestore
    //listener to changes that we can then use in the Activity
    private var productListener = MutableLiveData<MutableList<Product>>()


    fun getData(): MutableLiveData<MutableList<Product>> {
        if (products.isEmpty())
            createTestData()
        productListener.value = products //we inform the listener we have new data
        return productListener
    }

    fun createTestData()
    {
        val product1 = Product("Pasta", "10kr", "3", )
        val product2 = Product("Pasta", "10kr", "3", )
        val product3 = Product("Pasta", "10kr", "3", )


        //add some products to the products list - for testing purposes

        products.add(product1)
        products.add(product2)
        products.add(product3)
    }

    fun deleteProduct(index: Int) {
        products.removeAt(index)
    }

    fun deleteAllProducts(){
        products.clear()
        productListener.value= products
    }

    fun addProduct(product:Product) {
        db = Firebase.firestore
        products.add(product)
        productListener.value = products
        db.collection("products")
            .add(product)
            .addOnSuccessListener { documentReference ->
                Log.d("Error", "DocumentSnapshot written with ID: " + documentReference.id)
                product.id = documentReference.id            }
            .addOnFailureListener{ e ->
                Log.w("Error", "Error adding document", e)
            }
    }
}
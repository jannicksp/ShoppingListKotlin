package com.example.shoppingliststartcodekotlin.data

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase

object Repository {
    var products = mutableListOf<Product>()
    private lateinit var db: FirebaseFirestore
    //listener to changes that we can then use in the Activity
    private var productListener = MutableLiveData<MutableList<Product>>()


    fun getData(): MutableLiveData<MutableList<Product>> {
        if (products.isEmpty())
            //readDataFromFireBase()
            addRealTimeListener()
        productListener.value = products //we inform the listener we have new data
        return productListener
    }

    fun deleteProduct(index: Int) {
        db = Firebase.firestore
        val product = products[index]
        db.collection("products").document(product.id).delete().addOnSuccessListener {
            Log.d("Snapshot","DocumentSnapshot with id: ${product.id} successfully deleted!")
            //products.removeAt(index) //removes it from the list
        }
            .addOnFailureListener { e -> Log.w("Error", "Error deleting document", e) }
    }

    fun deleteAllProducts(): MutableLiveData<MutableList<Product>>{
        db = Firebase.firestore
        productListener.value = products
        for (product in products){
            db.collection("products").document(product.id).delete().addOnSuccessListener {
                Log.d("Snapshot","DocumentSnapshot with id: ${product.id} successfully deleted!")
                // products.removeAt(index) //removes it from the list
            }
                .addOnFailureListener { e -> Log.w("Error", "Error deleting document", e) }
        }
        products.clear()
        productListener.value = products
        return productListener

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

    fun incrementByOne(position: Int){
        val product = products[position]
        product.quantity++
        db = Firebase.firestore
        db.collection("products").document(product.id)
                .update( "quantity", FieldValue.increment(1))
    }

    fun decrementByOne(position: Int){
            val product = products[position]
            product.quantity--
            db = Firebase.firestore
            db.collection("products").document(product.id)
                    .update("quantity", FieldValue.increment(-1))
    }

    private fun addRealTimeListener()
    {
        val db = Firebase.firestore
        val docRef = db.collection("products")
        docRef.addSnapshotListener { snapshot, e ->
            if (e != null) {  //any errors
                Log.d("Repository", "Listen failed.", e)
                return@addSnapshotListener
            }
            products.clear() //to avoid duplicates.
            for (document in snapshot?.documents!!) { //add all products to the list
                Log.d("Repository_snapshotlist", "${document.id} => ${document.data}")
                val product = document.toObject<Product>()!!
                product.id = document.id
                products.add(product)
            }

            productListener.value = products
        }
    }


}
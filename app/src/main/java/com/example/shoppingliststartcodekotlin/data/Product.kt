package com.example.shoppingliststartcodekotlin.data

import com.google.firebase.firestore.Exclude

data class Product(var name:String = "", var price:String="", var quantity:String="", @get:Exclude var id: String = "" ) {}



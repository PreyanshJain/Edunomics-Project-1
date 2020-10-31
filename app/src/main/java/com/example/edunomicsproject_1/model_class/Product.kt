package com.example.edunomicsproject_1.model_class

import android.os.Parcel
import android.os.Parcelable
import androidx.versionedparcelable.ParcelField


class ProductDetails(
    val uid: String?,
    val product: String?,
    val productDetails: String?,
    val productPrice: String?
){
    constructor( ) : this( "", "" ,"","")
}

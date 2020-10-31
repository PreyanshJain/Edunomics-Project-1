package com.example.edunomicsproject_1.product

import android.app.Activity
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import com.example.edunomicsproject_1.R
import com.example.edunomicsproject_1.model_class.ProductDetails
import com.google.firebase.database.FirebaseDatabase

class ProductActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product)

        val productImage = findViewById<ImageView>(R.id.product_image)
        productImage.setOnClickListener {
            Log.d("ProductActivityLog", "Product image selector")
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            startActivityForResult(intent, 0)
        }

        val addBtn = findViewById<Button>(R.id.add_product_btn)
        addBtn.setOnClickListener {
            addProduct()
            finish()
        }

    }

    private var selectImageUri: Uri? = null


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        val productImage = findViewById<ImageView>(R.id.product_image)
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 0 && resultCode == Activity.RESULT_OK && data != null) {
            Log.d("ProductActivityLog", "Picture selected")
            selectImageUri = data.data
            val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, selectImageUri)
            productImage.setImageBitmap(bitmap)
        }
    }

    private fun addProduct() {
        val product = findViewById<EditText>(R.id.product).text.toString()
        val productDetails = findViewById<EditText>(R.id.product_details).text.toString()
        val productPrice = findViewById<EditText>(R.id.product_price).text.toString()

        Log.d("ProductActivityLog", "Product : $product")
        if (product.isEmpty() or productDetails.isEmpty() or productPrice.isEmpty()) {
            Toast.makeText(this, "Please enter all the above information", Toast.LENGTH_LONG).show()
            return
        }

        val ref = FirebaseDatabase.getInstance().getReference("Products")
        val productID = ref.push().key

        val productDetail =
            productID?.let {
                ProductDetails(
                    it,
                    product,
                    productDetails,
                    productPrice
                )
            }
        if (productID != null) {
            ref.child(productID).setValue(productDetail)
                .addOnCompleteListener{
                    Log.d("ProductActivityLog","Successfully saved Product")
                    Toast.makeText(this, "Successfully saved Product", Toast.LENGTH_LONG).show()
                }
        }
    }
}
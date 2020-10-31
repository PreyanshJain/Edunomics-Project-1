package com.example.edunomicsproject_1

import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.edunomicsproject_1.model_class.ProductDetails
import com.example.edunomicsproject_1.product.ProductActivity
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Item

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        fetchProducts()


        val floatingActionBtn = findViewById<FloatingActionButton>(R.id.floating_action_button)
        floatingActionBtn.setOnClickListener {
            val intent = Intent(this, ProductActivity::class.java)
            startActivity(intent)
        }
    }
    private fun fetchProducts() {
        val ref = FirebaseDatabase.getInstance().getReference("/Products")
        ref.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val adapter = GroupAdapter<GroupieViewHolder>()
                snapshot.children.forEach {
                    val product = it.getValue(ProductDetails::class.java)
                    if (product != null) {
                        adapter.add(ProductItem(product))
                    }

                }
                val recyclerView = findViewById<RecyclerView>(R.id.recycler_view)
                recyclerView.adapter = adapter
            }

            override fun onCancelled(error: DatabaseError) {
            }
        })
    }
}

class ProductItem(private val product: ProductDetails) : Item<GroupieViewHolder>() {
    override fun getLayout(): Int {
        return R.layout.list_view
    }

    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
        viewHolder.itemView.findViewById<TextView>(R.id.product_list_view).text = product.product
        viewHolder.itemView.findViewById<TextView>(R.id.product_details_list_view).text =
            product.productDetails
        viewHolder.itemView.findViewById<TextView>(R.id.product_price_list_view).text =
            product.productPrice
    }

}
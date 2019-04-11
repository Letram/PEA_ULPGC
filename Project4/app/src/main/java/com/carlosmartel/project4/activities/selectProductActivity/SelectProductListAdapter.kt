package com.carlosmartel.project4.activities.selectProductActivity

import android.graphics.Color
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.carlosmartel.project4.R
import com.carlosmartel.project4.data.entities.Product
import kotlinx.android.synthetic.main.select_product_item.view.*

class SelectProductListAdapter: RecyclerView.Adapter<SelectProductListAdapter.SelectProductViewHolder>(){
    private var products: List<Product> = ArrayList()
    private lateinit var listener: OnProductClickListener
    private var productSelected: Product? = null

    class SelectProductViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(product: Product, listener: OnProductClickListener) {
            itemView.customer_item_name.text = product.p_name
            itemView.product_item_price.text = product.price.toString()
            itemView.setOnClickListener {
                if (adapterPosition != RecyclerView.NO_POSITION) {
                    listener.onProductClick(product)
                }
            }
        }
    }

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): SelectProductViewHolder {
        val selectProductView = LayoutInflater.from(p0.context).inflate(R.layout.select_product_item, p0, false)
        return SelectProductViewHolder(
            selectProductView
        )
    }

    override fun getItemCount(): Int {
        return products.size
    }

    fun setProductSelected(product: Product) {
        productSelected = product
        this.notifyDataSetChanged()
    }

    override fun onBindViewHolder(selectProductViewHolder: SelectProductViewHolder, position: Int) {
        if (productSelected?.p_id == products[position].p_id) selectProductViewHolder.itemView.setBackgroundColor(
            Color.LTGRAY)
        else selectProductViewHolder.itemView.setBackgroundColor(Color.WHITE)
        selectProductViewHolder.bind(product = products[position], listener = listener)
    }

    fun setProducts(products: List<Product>) {
        this.products = products
    }

    fun setOnProductClickListener(listener: OnProductClickListener) {
        this.listener = listener
    }

    interface OnProductClickListener {
        fun onProductClick(product: Product)
    }
}

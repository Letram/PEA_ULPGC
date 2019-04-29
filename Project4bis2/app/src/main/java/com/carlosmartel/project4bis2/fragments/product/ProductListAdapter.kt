package com.carlosmartel.project4bis2.fragments.product

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.carlosmartel.project4bis2.R
import com.carlosmartel.project4bis2.data.entities.Product
import kotlinx.android.synthetic.main.product_list_item.view.*

class ProductListAdapter : RecyclerView.Adapter<ProductListAdapter.ProductViewHolder>() {

    private var products: List<Product> = ArrayList()
    private lateinit var listener: OnItemClickListener

    class ProductViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(product: Product, clickListener: OnItemClickListener) {
            itemView.productNameLabel.text = product.p_name
            itemView.productPriceLabel.text = product.price.toString()


            itemView.setOnClickListener {
                if (adapterPosition != RecyclerView.NO_POSITION)
                    clickListener.onItemClick(product)
            }
            itemView.setOnLongClickListener {
                if (adapterPosition != RecyclerView.NO_POSITION)
                    clickListener.onItemLongClick(product)
                true
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val productView = LayoutInflater.from(parent.context).inflate(R.layout.product_list_item, parent, false)
        return ProductViewHolder(productView)
    }

    override fun getItemCount(): Int {
        return products.size
    }

    override fun onBindViewHolder(productViewHolder: ProductViewHolder, position: Int) {
        productViewHolder.bind(products[position], listener)
    }

    fun setProducts(products: List<Product>){
        this.products = products
    }

    fun getProductAt(position: Int): Product{
        return products[position]
    }

    interface OnItemClickListener{
        fun onItemClick(product: Product)
        fun onItemLongClick(product: Product)
    }

    fun setOnItemClickListener(listener: OnItemClickListener){
        this.listener = listener
    }
}

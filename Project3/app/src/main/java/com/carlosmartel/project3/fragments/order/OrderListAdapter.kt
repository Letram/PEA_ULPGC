package com.carlosmartel.project3.fragments.order

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.carlosmartel.project3.R
import com.carlosmartel.project3.data.models.Order
import kotlinx.android.synthetic.main.order_list_item.view.*

class OrderListAdapter : RecyclerView.Adapter<OrderListAdapter.OrderViewHolder>(){
    private var orders: List<Order> = ArrayList()
    private lateinit var listener: OnItemClickListener
    class OrderViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(order: Order, clickListener: OnItemClickListener) {
            itemView.order_customer_name.text = order.uid.toString()
            itemView.order_product_name.text = order.productID.toString()


            itemView.setOnClickListener {
                if (adapterPosition != RecyclerView.NO_POSITION)
                    clickListener.onItemClick(order)
            }
            itemView.setOnLongClickListener {
                if (adapterPosition != RecyclerView.NO_POSITION)
                    clickListener.onItemLongClick(order)
                true
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderListAdapter.OrderViewHolder {
        val orderView = LayoutInflater.from(parent.context).inflate(R.layout.order_list_item, parent, false)
        return OrderViewHolder(orderView)
    }

    override fun getItemCount(): Int {
        return orders.size
    }

    override fun onBindViewHolder(orderViewHolder: OrderListAdapter.OrderViewHolder, position: Int) {
        orderViewHolder.bind(orders[position], listener)
    }
    
    fun setOrders(orders: List<Order>){
        this.orders = orders
    }

    fun getOrderAt(position: Int): Order{
        return orders[position]
    }

    interface OnItemClickListener{
        fun onItemClick(order: Order)
        fun onItemLongClick(order: Order)
    }

    fun setOnItemClickListener(listener: OnItemClickListener){
        this.listener = listener
    }
}

package com.carlosmartel.project4.fragments.order

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.carlosmartel.project4.R
import com.carlosmartel.project4.data.entities.Order
import com.carlosmartel.project4.data.pojo.InflatedOrder
import com.carlosmartel.project4.data.pojo.InflatedOrderJson
import kotlinx.android.synthetic.main.order_list_item.view.*

class OrderListAdapterJson : RecyclerView.Adapter<OrderListAdapterJson.OrderViewHolder>() {

    private var orders: List<Order> = ArrayList()
    private var inflatedOrders: List<InflatedOrderJson> = ArrayList()

    private lateinit var listener: OnItemClickListener
    private lateinit var inflatedListener: OnInflatedItemClickListener

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

        fun bindInflated(inflatedOrder: InflatedOrderJson, listener: OnInflatedItemClickListener) {
            itemView.order_customer_name.text = inflatedOrder.customerName
            itemView.order_product_name.text = inflatedOrder.productName
            itemView.order_code.text = inflatedOrder.order.code

            itemView.setOnClickListener {
                if (adapterPosition != RecyclerView.NO_POSITION)
                    listener.onItemClick(inflatedOrder)
            }
            itemView.setOnLongClickListener {
                if (adapterPosition != RecyclerView.NO_POSITION)
                    listener.onItemLongClick(inflatedOrder)
                true
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderViewHolder {
        val orderView = LayoutInflater.from(parent.context).inflate(R.layout.order_list_item, parent, false)
        return OrderViewHolder(orderView)
    }

    override fun getItemCount(): Int {
        return inflatedOrders.size
    }

    override fun onBindViewHolder(orderViewHolder: OrderViewHolder, position: Int) {
        orderViewHolder.bindInflated(inflatedOrders[position], inflatedListener)
    }

    fun setOrders(orders: List<Order>) {
        this.orders = orders
    }

    fun getOrderAt(position: Int): Order {
        return orders[position]
    }


    interface OnItemClickListener {
        fun onItemClick(order: Order)
        fun onItemLongClick(order: Order)
    }


    interface OnInflatedItemClickListener {
        fun onItemClick(inflatedOrder: InflatedOrderJson)
        fun onItemLongClick(inflatedOrder: InflatedOrderJson)
    }

    fun setOnItemClickListener(listener: OnItemClickListener) {
        this.listener = listener
    }

    fun setOnInflatedItemClickListener(listener: OnInflatedItemClickListener) {
        this.inflatedListener = listener
    }

    fun setInflatedOrders(inflatedOrders: List<InflatedOrderJson>) {
        this.inflatedOrders = inflatedOrders
    }
}

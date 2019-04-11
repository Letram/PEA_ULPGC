package com.carlosmartel.project4.fragments.customer

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.carlosmartel.project4.R
import com.carlosmartel.project4.data.entities.Customer
import kotlinx.android.synthetic.main.customer_list_item.view.*

class CustomerListAdapter : RecyclerView.Adapter<CustomerListAdapter.CustomerViewHolder>() {

    private var customers: List<Customer> = ArrayList()
    private lateinit var listener: OnItemClickListener

    class CustomerViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(customer: Customer, clickListener: OnItemClickListener) {
            itemView.customerName.text = customer.c_name
            itemView.customerAddress.text = customer.address

            itemView.setOnClickListener {
                if (adapterPosition != RecyclerView.NO_POSITION)
                    clickListener.onItemClick(customer)
            }
            itemView.setOnLongClickListener {
                if (adapterPosition != RecyclerView.NO_POSITION)
                    clickListener.onItemLongClick(customer)
                true
            }
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomerViewHolder {
        val customerView = LayoutInflater.from(parent.context).inflate(R.layout.customer_list_item, parent, false)

        return CustomerViewHolder(customerView)
    }

    override fun getItemCount(): Int {
        return customers.size
    }

    override fun onBindViewHolder(customerViewHolder: CustomerViewHolder, position: Int) {
        customerViewHolder.bind(customers[position], listener)

    }

    fun setCustomers(customers: List<Customer>) {
        this.customers = customers
        notifyDataSetChanged()
    }

    fun getCustomerAt(position: Int): Customer {
        return customers[position]
    }

    interface OnItemClickListener {
        fun onItemClick(customer: Customer)
        fun onItemLongClick(customer: Customer)
    }

    fun setOnItemClickListener(listener: OnItemClickListener) {
        this.listener = listener
    }
}
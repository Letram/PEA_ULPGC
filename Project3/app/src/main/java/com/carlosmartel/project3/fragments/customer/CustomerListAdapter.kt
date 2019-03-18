package com.carlosmartel.project3.fragments.customer

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.carlosmartel.project3.R
import com.carlosmartel.project3.data.models.Customer

class CustomerListAdapter(private val customers: List<Customer>):
RecyclerView.Adapter<CustomerListAdapter.CustomerViewHolder>(){

    class CustomerViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var customerName: TextView = itemView.findViewById(R.id.customerName)
        var customerAddress: TextView = itemView.findViewById(R.id.customerAddress)
        var customerID: TextView = itemView.findViewById(R.id.customerID)

    }



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomerViewHolder {
        val customerView = LayoutInflater.from(parent.context).inflate(R.layout.customer_list_item, parent, false)
        return CustomerViewHolder(customerView)
    }

    override fun getItemCount(): Int {
        return customers.size
    }

    override fun onBindViewHolder(customerViewHolder: CustomerViewHolder, position: Int) {
        val currentCustomer = customers[position]
        customerViewHolder.customerName.text = currentCustomer.name
        customerViewHolder.customerAddress.text = currentCustomer.address
        customerViewHolder.customerID.text = currentCustomer.uid.toString()
    }
}
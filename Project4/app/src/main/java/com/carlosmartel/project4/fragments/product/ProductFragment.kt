package com.carlosmartel.project4.fragments.product

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AlertDialog
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.carlosmartel.project4.R
import com.carlosmartel.project4.data.entities.Product
import com.carlosmartel.project4.data.json.backend.productJson.JsonProductService
import com.carlosmartel.project4.data.json.backend.productJson.ProductAPIController
import com.carlosmartel.project4.data.pojo.InflatedOrderJson
import com.carlosmartel.project4.fragments.order.OrderViewModel

class ProductFragment : Fragment() {

    private var listener: OnFragmentInteractionListener? = null
    private lateinit var productViewModel: ProductViewModel
    private lateinit var orderViewModel: OrderViewModel

    private lateinit var recyclerView: RecyclerView
    private lateinit var recyclerAdapter: ProductListAdapter

    private lateinit var productsWithOrders: List<Int>
    private lateinit var orders: List<InflatedOrderJson>

    private lateinit var jsonProductAPI: ProductAPIController

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_product, container, false)

        recyclerView = view.findViewById(R.id.product_recycler_view)
        recyclerAdapter = ProductListAdapter()
        jsonProductAPI = ProductAPIController(JsonProductService())
        recyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = recyclerAdapter
        }
        productViewModel =
            ViewModelProviders.of(this.activity!!).get(ProductViewModel(activity!!.application)::class.java)
        orderViewModel = ViewModelProviders.of(this.activity!!).get(OrderViewModel(activity!!.application)::class.java)

        productViewModel.getAllProductsJSON().observe(this, Observer {
            if (it != null) {
                recyclerAdapter.setProducts(it)
                recyclerAdapter.notifyDataSetChanged()
            }
        })
        orderViewModel.getAllInflatedOrdersJSON().observe(this, Observer {
            if (it != null) {
                orders = it
            }
        })
        recyclerAdapter.setOnItemClickListener(object : ProductListAdapter.OnItemClickListener {
            override fun onItemClick(product: Product) {
                listener?.updateProduct(product)
            }

            override fun onItemLongClick(product: Product) {
                for (infOrder in orders) {
                    if (infOrder.order.productID == product.p_id) {
                        openDialog()
                        return
                    }
                }
                listener?.deleteProduct(product)
            }
        })
        return view
    }


    private fun openDialog() {
        val dialog = AlertDialog.Builder(activity!!)
        dialog.setTitle(R.string.dialog_product_title)
        dialog.setMessage(R.string.dialog_cant_delete_product)
        dialog.setPositiveButton(R.string.OK) { _, _ -> }
        dialog.setCancelable(false)
        dialog.show()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnFragmentInteractionListener) {
            listener = context
        } else {
            throw RuntimeException(context.toString() + " must implement OnProductFragmentInteractionListener")
        }
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }


    interface OnFragmentInteractionListener {
        fun updateProduct(product: Product)
        fun deleteProduct(product: Product)
    }

    companion object {

        @JvmStatic
        fun newInstance() = ProductFragment()
    }
}

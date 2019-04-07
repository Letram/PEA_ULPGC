package com.carlosmartel.project3.fragments.product

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
import com.carlosmartel.project3.R
import com.carlosmartel.project3.data.entities.Product

class ProductFragment : Fragment() {

    private var listener: OnFragmentInteractionListener? = null
    private lateinit var productViewModel: ProductViewModel
    private lateinit var recyclerView: RecyclerView
    private lateinit var recyclerAdapter: ProductListAdapter
    private lateinit var productsWithOrders: List<Int>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_product, container, false)

        recyclerView = view.findViewById(R.id.product_recycler_view)
        recyclerAdapter = ProductListAdapter()

        recyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = recyclerAdapter
        }
        productViewModel = ViewModelProviders.of(this.activity!!)
            .get(ProductViewModel(application = activity!!.application)::class.java)
        productViewModel.getAllProducts().observe(this, Observer {
            if (it != null) {
                recyclerAdapter.setProducts(products = it)
                recyclerAdapter.notifyDataSetChanged()
            }
        })
        productViewModel.getAllProductsWithOrders().observe(this, Observer {
            if(it != null)
                productsWithOrders = it
        })
/*
        //In order to delete a customer we just have to swipe left or right
        ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(
            0,
            ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
        ) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val undoProduct = recyclerAdapter.getProductAt(viewHolder.adapterPosition)
                productViewModel.delete(recyclerAdapter.getProductAt(viewHolder.adapterPosition))
                Snackbar.make(view!!, R.string.customer_deleted, Snackbar.LENGTH_SHORT)
                    .setAction(R.string.snack_undo) {
                        productViewModel.insert(undoProduct)
                    }
                    .show()
            }
        }).attachToRecyclerView(recyclerView)
*/
        recyclerAdapter.setOnItemClickListener(object : ProductListAdapter.OnItemClickListener {
            override fun onItemClick(product: Product) {
                listener?.updateProduct(product)
            }

            override fun onItemLongClick(product: Product) {
                if(productsWithOrders.contains(product.p_id))
                    openDialog()
                else
                    listener?.deleteProduct(product)
            }
        })
        return view
    }


    private fun openDialog(){
        val dialog = AlertDialog.Builder(activity!!)
        dialog.setTitle(R.string.dialog_product_title)
        dialog.setMessage(R.string.dialog_cant_delete_product)
        dialog.setPositiveButton(R.string.OK){ _, _ -> }
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

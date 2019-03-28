package com.carlosmartel.project3.fragments.product

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.helper.ItemTouchHelper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.carlosmartel.project3.R
import com.carlosmartel.project3.data.models.Product

class ProductFragment : Fragment() {

    private var listener: OnFragmentInteractionListener? = null
    private lateinit var productViewModel: ProductViewModel
    private lateinit var recyclerView: RecyclerView
    private lateinit var recyclerAdapter: ProductListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_product, container, false)

        recyclerView = view.findViewById(R.id.recycler_view)
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

        recyclerAdapter.setOnItemClickListener(object : ProductListAdapter.OnItemClickListener {
            override fun onItemClick(product: Product) {
                listener?.updateProduct(product)
            }

            override fun onItemLongClick(product: Product) {
                listener?.deleteProduct(product)
            }

        })
        return view
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnFragmentInteractionListener) {
            listener = context
        } else {
            throw RuntimeException(context.toString() + " must implement OnProductFragmentInteractionListener") as Throwable
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

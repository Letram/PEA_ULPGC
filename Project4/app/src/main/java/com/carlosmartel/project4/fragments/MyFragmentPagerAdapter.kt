package com.carlosmartel.project4.fragments

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import com.carlosmartel.project4.fragments.customer.CustomerFragment
import com.carlosmartel.project4.fragments.order.OrderFragment
import com.carlosmartel.project4.fragments.product.ProductFragment

class MyFragmentPagerAdapter(fm: FragmentManager): FragmentPagerAdapter(fm){
    private var titles: List<String> = ArrayList()

    override fun getPageTitle(position: Int): CharSequence? {
        return titles[position]
    }

    override fun getItem(index: Int): Fragment {
        println("INDEX $index")
        return when (index){
            0 -> CustomerFragment.newInstance()
            1 -> OrderFragment.newInstance()
            else -> ProductFragment.newInstance()
        }
    }

    override fun getCount(): Int {
        return 3
    }

    fun setTitles(titles: Array<String>){
        this.titles = titles.clone().toList()
    }
}
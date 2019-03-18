package com.carlosmartel.project3.fragments

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import com.carlosmartel.project3.fragments.customer.CustomerListFragment

class MyFragmentPagerAdapter(fm: FragmentManager): FragmentPagerAdapter(fm){

    override fun getItem(index: Int): Fragment {
        return when (index){
            0 -> CustomerListFragment.newInstance()
            1 -> CustomerListFragment.newInstance()
            else -> CustomerListFragment.newInstance()
        }
    }

    override fun getCount(): Int {
        return 3
    }

}
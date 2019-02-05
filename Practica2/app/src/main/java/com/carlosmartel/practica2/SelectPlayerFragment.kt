package com.carlosmartel.practica2

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

class SelectPlayerFragment : Fragment(){
    companion object {

        fun newInstance(): SelectPlayerFragment {
            return SelectPlayerFragment()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.select_player_fragment, container, false)
    }
}
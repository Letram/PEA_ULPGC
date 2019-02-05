package com.carlosmartel.practica2

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

class PlayerTurnInfoFragment : Fragment(){
    companion object {

        fun newInstance(): PlayerTurnInfoFragment {
            return PlayerTurnInfoFragment()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.player_turn_info_fragment, container, false)
    }
}
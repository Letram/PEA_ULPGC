package com.carlosmartel.practica2

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

class PlayerScoreFragment : Fragment(){
    companion object {

        fun newInstance(): PlayerScoreFragment {
            return PlayerScoreFragment()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.player_score_fragment, container, false)
    }
}
package com.carlosmartel.practica2.Fragments

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.carlosmartel.practica2.R
import kotlinx.android.synthetic.main.player_score_fragment.*

class PlayerScoreFragment : Fragment(){
    companion object {
        fun newInstance(): PlayerScoreFragment {
            return PlayerScoreFragment()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.player_score_fragment, container, false)
    }

    fun setPlayerName(gameMode: String){
        var newName = -1
        if(gameMode == "Single") newName = R.string.machine;
        else newName = R.string.player2
        otherPlayer.setText(newName)
    }

}
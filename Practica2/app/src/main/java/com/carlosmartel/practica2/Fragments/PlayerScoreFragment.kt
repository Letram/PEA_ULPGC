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
    private var playerScoreInterface : PlayerScoreInterface? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.player_score_fragment, container, false)
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        try{
            playerScoreInterface = context as PlayerScoreInterface
            print("CAST DONE")
        } catch (e: ClassCastException){
            throw ClassCastException(context?.toString() + "must implement PlayerScoreInterface")
        }
    }
    interface PlayerScoreInterface {
        fun setScores()
    }
}
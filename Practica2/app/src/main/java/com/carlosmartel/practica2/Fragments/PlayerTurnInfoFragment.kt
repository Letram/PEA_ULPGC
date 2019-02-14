package com.carlosmartel.practica2.Fragments

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import com.carlosmartel.practica2.R
import kotlinx.android.synthetic.main.player_turn_info_fragment.*

class PlayerTurnInfoFragment : Fragment(){

    private var playerTurnInfoInterface: PlayerTurnInfoInterface? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.player_turn_info_fragment, container, false)

        val rollB = view?.findViewById<TextView>(R.id.rollBtn)
        rollB?.setOnClickListener{
            playerTurnInfoInterface?.roll()
        }
        return view
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        try{
            playerTurnInfoInterface = context as PlayerTurnInfoInterface
            print("CAST DONE")
        } catch (e: ClassCastException){
            throw ClassCastException(context?.toString() + "must implement PlayerTurnInfoInterface")
        }
    }
    interface PlayerTurnInfoInterface{
        fun roll()
        fun collect()
        fun setAccScore()
    }
}
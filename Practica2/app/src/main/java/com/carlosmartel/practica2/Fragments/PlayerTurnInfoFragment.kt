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
import kotlinx.android.synthetic.main.activity_game.*
import kotlinx.android.synthetic.main.player_turn_info_fragment.*

class PlayerTurnInfoFragment : Fragment(){

    private var playerTurnInfoInterface: PlayerTurnInfoInterface? = null
    private var accScore = 0
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.player_turn_info_fragment, container, false)

        val rollB = view?.findViewById<TextView>(R.id.rollBtn)
        rollB?.setOnClickListener{
            playerTurnInfoInterface?.roll()
        }

        val collectB = view?.findViewById<TextView>(R.id.collectBtn)
        collectB?.setOnClickListener{
            playerTurnInfoInterface?.collect()
        }

        view?.findViewById<TextView>(R.id.playerName)?.text = getString(R.string.player, resources.getInteger(R.integer.currentPlayer))

        view?.findViewById<TextView>(R.id.pointsInTurnLabel)?.text = getString(R.string.pointsInTurnLabel, accScore)

        return view
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        try{
            playerTurnInfoInterface = context as PlayerTurnInfoInterface
        } catch (e: ClassCastException){
            throw ClassCastException(context?.toString() + "must implement PlayerTurnInfoInterface")
        }
    }

    fun setAccValue(diceValue: Int) {
        accScore += diceValue
        updateTexts()
    }

    private fun updateTexts(){
        if(playerTurnInfoInterface != null)
            playerName.text = getString(R.string.player, playerTurnInfoInterface?.currentPlayer)
        else
            playerName.text = getString(R.string.player, 1)
        pointsInTurnLabel.text = getString(R.string.pointsInTurnLabel, accScore)
    }

    fun resetAccScore() {
        accScore = 0
        updateTexts()
    }

    interface PlayerTurnInfoInterface{

        var currentPlayer: Int

        fun roll()
        fun collect()
        fun setAccScore()
    }
}
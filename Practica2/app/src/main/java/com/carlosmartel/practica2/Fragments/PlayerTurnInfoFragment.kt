package com.carlosmartel.practica2.Fragments

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.carlosmartel.practica2.CustomData
import com.carlosmartel.practica2.R
import kotlinx.android.synthetic.main.player_turn_info_fragment.*

class PlayerTurnInfoFragment : Fragment(){

    private var playerTurnInfoInterface: PlayerTurnInfoInterface? = null
    private var accScore = 0

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.player_turn_info_alt, container, false)

        val rollB = view?.findViewById<TextView>(R.id.rollBtn)
        rollB?.setOnClickListener{
            if(collectBtn.visibility == View.INVISIBLE)collectBtn.visibility = View.VISIBLE
            playerTurnInfoInterface?.onRollBtnPressed()
        }

        val collectB = view?.findViewById<TextView>(R.id.collectBtn)
        collectB?.setOnClickListener{
            collectBtn.visibility = View.INVISIBLE
            playerTurnInfoInterface?.onCollectBtnPressed(accScore)
        }

        view?.findViewById<TextView>(R.id.playerName)?.text = getString(R.string.player, resources.getInteger(R.integer.currentPlayer))

        view?.findViewById<TextView>(R.id.pointsInTurnLabel)?.text = getString(R.string.pointsInTurnLabel, accScore)

        return view
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt(CustomData.ACC_SCORE, accScore)
        outState.putInt(CustomData.CURRENT_PLAYER, if(playerTurnInfoInterface != null) playerTurnInfoInterface!!.currentPlayer else 1)
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)
        if(savedInstanceState != null){
            accScore = savedInstanceState.getInt(CustomData.ACC_SCORE)
            pointsInTurnLabel.text = getString(R.string.pointsInTurnLabel, accScore)
        }
    }
    override fun onAttach(context: Context?) {
        super.onAttach(context)
        try{
            playerTurnInfoInterface = context as PlayerTurnInfoInterface
        } catch (e: ClassCastException){
            throw ClassCastException(context?.toString() + "must implement PlayerTurnInfoInterface")
        }
    }

    fun setAccValue(diceValue: Int):Int {
        accScore = if(diceValue == 1) 0 else accScore + diceValue
        updateTexts()
        return accScore
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

    fun hideBtns() {
        collectBtn.visibility = View.INVISIBLE
        rollBtn.visibility = View.INVISIBLE
    }

    fun showBtns(){
        collectBtn.visibility = View.VISIBLE
        rollBtn.visibility = View.VISIBLE
    }

    interface PlayerTurnInfoInterface{

        var currentPlayer: Int

        fun onRollBtnPressed():Boolean
        fun onCollectBtnPressed(scoreValue: Int)
    }
}
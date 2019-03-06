package com.carlosmartel.practica2.Fragments

import android.animation.ObjectAnimator
import android.animation.PropertyValuesHolder
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
    private var scaleDown: ObjectAnimator = ObjectAnimator()
    private var rollB : TextView? = null
    private var collectB : TextView? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.player_turn_info_alt, container, false)

        rollB = view?.findViewById(R.id.rollBtn)
        rollB?.setOnClickListener{
            playerTurnInfoInterface?.roll()
        }

        collectB = view?.findViewById(R.id.collectBtn)
        collectB?.visibility = if(accScore == 0) View.GONE else View.VISIBLE
        collectB?.setOnClickListener{
            playerTurnInfoInterface?.collect(accScore)
        }

        view?.findViewById<TextView>(R.id.playerName)?.text = getString(R.string.player, resources.getInteger(R.integer.currentPlayer))

        view?.findViewById<TextView>(R.id.pointsInTurnLabel)?.text = getString(R.string.pointsInTurnLabel, accScore)

        return view
    }

    private fun startTextAnimation(rollB: TextView) {
        scaleDown = ObjectAnimator.ofPropertyValuesHolder(
            rollB,
            PropertyValuesHolder.ofFloat("scaleX", 1.2f),
            PropertyValuesHolder.ofFloat("scaleY", 1.2f)
        )
        scaleDown.duration = 310

        scaleDown.repeatCount = ObjectAnimator.INFINITE
        scaleDown.repeatMode = ObjectAnimator.REVERSE

        scaleDown.start()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt(CustomData.ACC_SCORE, accScore)
        outState.putInt(CustomData.CURRENT_PLAYER, if(playerTurnInfoInterface != null) playerTurnInfoInterface!!.currentPlayer else 1)
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        if(savedInstanceState != null){
            accScore = savedInstanceState.getInt(CustomData.ACC_SCORE)
            collectB?.visibility = if(accScore == 0) View.GONE else View.VISIBLE
            pointsInTurnLabel.text = getString(R.string.pointsInTurnLabel, accScore)
        }
        super.onViewStateRestored(savedInstanceState)
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
        collectB?.visibility = if(accScore > 0) View.VISIBLE else View.GONE
        updateTexts()
        return accScore
    }
    fun resetAccScore() {
        accScore = 0
        updateTexts()
    }

    fun hideBtns() {
        collectB?.visibility = View.INVISIBLE
        rollB?.visibility = View.INVISIBLE
    }

    fun showBtns(){
        collectB?.visibility = View.VISIBLE
        rollB?.visibility = View.VISIBLE
    }

    fun stopRolling() {
        scaleDown.cancel()
        collectB?.isClickable = true
        rollB?.isClickable = true
        rollBtn?.text = getString(R.string.rollBtn)
    }

    fun startRolling() {
        startTextAnimation(rollBtn)
        collectB?.isClickable = false
        rollB?.isClickable = false
        rollB?.text = getString(R.string.rolling)
    }

    private fun updateTexts(){
        collectB?.visibility = if(accScore == 0) View.GONE else View.VISIBLE
        if(playerTurnInfoInterface != null)
            playerName?.text = getString(R.string.player, playerTurnInfoInterface?.currentPlayer)
        else
            playerName?.text = getString(R.string.player, 1)
        pointsInTurnLabel?.text = getString(R.string.pointsInTurnLabel, accScore)
    }

    interface PlayerTurnInfoInterface{

        var currentPlayer: Int

        fun roll()
        fun collect(scoreValue: Int)
    }
}
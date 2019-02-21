package com.carlosmartel.practica2.Fragments

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.carlosmartel.practica2.CustomData
import com.carlosmartel.practica2.R
import kotlinx.android.synthetic.main.player_score_fragment.*

class PlayerScoreFragment : Fragment(){


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        return inflater.inflate(R.layout.player_score_fragment, container, false)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putIntArray(CustomData.SCORES, intArrayOf(progressBar1.progress, progressBar2.progress))
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)
        val scores = if(savedInstanceState?.getIntArray(CustomData.SCORES) != null) savedInstanceState.getIntArray(CustomData.SCORES) else intArrayOf(0,0)
        updateBars(scores)
    }

    fun setScore(scores: IntArray){
        updateBars(scores)
    }

    private fun updateBars(ints: IntArray) {
        progressBar1.progress = ints[0]
        player1Score.text = ints[0].toString()
        progressBar2.progress = ints[1]
        otherPlayerScore.text = ints[1].toString()
    }

    fun setup(gameMode: String?) {

        progressBar1?.max = resources.getInteger(R.integer.goal)
        progressBar2?.max = resources.getInteger(R.integer.goal)
        player1?.text = getString(R.string.player, resources.getInteger(R.integer.currentPlayer))
        if(gameMode == "multi")
            otherPlayer.text = getString(R.string.player, resources.getInteger(R.integer.currentPlayer)+1)
        else
            otherPlayer.text = getString(R.string.machine)
    }

    fun reset() {
        updateBars(intArrayOf(0,0))
    }


}
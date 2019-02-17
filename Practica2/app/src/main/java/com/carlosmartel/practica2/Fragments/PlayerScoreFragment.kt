package com.carlosmartel.practica2.Fragments

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.carlosmartel.practica2.R
import kotlinx.android.synthetic.main.player_score_fragment.*

class PlayerScoreFragment : Fragment(){

    var scores = intArrayOf(0,0)

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        return inflater.inflate(R.layout.player_score_fragment, container, false)
    }

    fun setScore(player: Int, scoreToAdd: Int){
        scores[player-1] += scoreToAdd
        updateBars()
    }

    private fun updateBars() {
        progressBar1.progress = scores[0]
        player1Score.text = scores[0].toString()
        progressBar2.progress = scores[1]
        otherPlayerScore.text = scores[1].toString()
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


}
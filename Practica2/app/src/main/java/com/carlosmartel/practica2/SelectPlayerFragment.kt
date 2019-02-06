package com.carlosmartel.practica2

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import kotlinx.android.synthetic.*
import kotlinx.android.synthetic.main.select_player_fragment.*

class SelectPlayerFragment : Fragment(){

    companion object {
        const val EXTRA_GAMEMODE = "EXTRA_GAMEMODE"
        fun newInstance(): SelectPlayerFragment {
            return SelectPlayerFragment()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootView = inflater.inflate(R.layout.select_player_fragment, container, false)
        //TODO buttons are null. They are not created in the context.
        setListeners(singleBtn, multiBtn)
        return rootView
    }

    private fun setListeners(singleBtn: Button, multiBtn: Button) {
        singleBtn.setOnClickListener{
            startGame("Single")
        }
        multiBtn.setOnClickListener{
            startGame("Multiplayer")
        }
    }

    private fun startGame(mode: String) {
        val intent = Intent(this.activity, GameActivity::class.java)
        intent.putExtra(SelectPlayerFragment.EXTRA_GAMEMODE, mode)
        startActivity(intent)
    }


}
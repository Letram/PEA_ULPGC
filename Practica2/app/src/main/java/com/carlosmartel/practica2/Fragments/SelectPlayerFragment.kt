package com.carlosmartel.practica2.Fragments

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import com.carlosmartel.practica2.CustomData
import com.carlosmartel.practica2.GameActivity
import com.carlosmartel.practica2.R
import java.lang.ClassCastException

class SelectPlayerFragment : Fragment(){

    companion object {
        fun newInstance(): SelectPlayerFragment {
            return SelectPlayerFragment()
        }
    }
    var selectPlayerInterface : SelectPlayerInterface? = null
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view =  inflater.inflate(R.layout.select_player_fragment, container, false)
        val singlebtn = view?.findViewById<Button>(R.id.singleBtn)
        val multibtn = view?.findViewById<Button>(R.id.multiBtn)

        singlebtn?.setOnClickListener{ startNewGame("single")}
        multibtn?.setOnClickListener{ startNewGame("multi")}

        return view
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        try{
            selectPlayerInterface = context as SelectPlayerInterface
            print("CAST DONE")
        } catch (e: ClassCastException){
            throw ClassCastException(context?.toString() + "must implement SelectPlayerInterface")
        }
    }

    private fun startGame(mode: String) {
        val intent = Intent(this.activity, GameActivity::class.java)
        intent.putExtra(CustomData.EXTRA_GAMEMODE, mode)
        startActivity(intent)
    }

    private fun startNewGame(mode: String){
        selectPlayerInterface?.startGame(mode)
    }

    interface SelectPlayerInterface{
        fun startGame(mode: String)
    }

}
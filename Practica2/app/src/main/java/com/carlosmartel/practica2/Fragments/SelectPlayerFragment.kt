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
    private var selectPlayerInterface : SelectPlayerInterface? = null
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

    private fun startNewGame(mode: String){
        selectPlayerInterface?.startGame(mode)
    }

    interface SelectPlayerInterface{
        fun startGame(mode: String)
    }

}
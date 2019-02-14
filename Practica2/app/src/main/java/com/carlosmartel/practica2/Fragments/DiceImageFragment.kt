package com.carlosmartel.practica2.Fragments

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.carlosmartel.practica2.R
import kotlinx.android.synthetic.main.image_fragment.*
import kotlin.random.Random

class DiceImageFragment : Fragment(){
    var diceImages :IntArray = intArrayOf(R.drawable.dice_1, R.drawable.dice_2, R.drawable.dice_3, R.drawable.dice_4, R.drawable.dice_5, R.drawable.dice_6)

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.image_fragment, container, false)
    }


    fun getNumber():Int{
        //todo change this to resource
        val diceValue = Random.nextInt(
            1,
            6
        )
        diceImage.setImageResource(diceImages[diceValue-1])
        diceValueLabel.text = getString(R.string.diceValueLabel, diceValue)
        return diceValue
    }
}
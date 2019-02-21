package com.carlosmartel.practica2.Fragments

import android.graphics.Bitmap
import android.graphics.drawable.AnimationDrawable
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.os.Handler
import android.os.Parcelable
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.carlosmartel.practica2.CustomData
import com.carlosmartel.practica2.R
import kotlinx.android.synthetic.main.image_fragment.*
import kotlin.random.Random

class DiceImageFragment : Fragment(){
    private var diceImages :IntArray = intArrayOf(R.drawable.dice_1, R.drawable.dice_2, R.drawable.dice_3, R.drawable.dice_4, R.drawable.dice_5, R.drawable.dice_6)
    private var diceValue :Int = -1

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.image_fragment, container, false)

        view?.findViewById<TextView>(R.id.diceValueLabel)?.visibility = View.INVISIBLE

        return view
    }


    fun getNumber():Int{
        diceValueLabel.visibility = View.VISIBLE
        diceImage.setImageResource(android.R.color.transparent)
        Handler().postDelayed({},5000)
        diceValue = Random.nextInt(
            resources.getInteger(R.integer.diceMin),
            resources.getInteger(R.integer.diceMax)+1
        )
        diceImage.setImageResource(diceImages[diceValue-1])
        diceValueLabel.text = getString(R.string.diceValueLabel, diceValue)
        return diceValue
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt(CustomData.DICE_VALUE, diceValue)
        if(diceImage.drawable != null && diceImage.drawable is BitmapDrawable){
            outState.putParcelable(CustomData.DICE_IMAGE, (diceImage.drawable as BitmapDrawable).bitmap as Parcelable)
        }
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)
        if(savedInstanceState != null){
            diceValue = savedInstanceState.getInt(CustomData.DICE_VALUE)
            diceValueLabel.text = resources.getString(R.string.diceValueLabel, diceValue)
            diceValueLabel.visibility = View.VISIBLE
            val parcelable : Parcelable? = savedInstanceState.getParcelable(CustomData.DICE_IMAGE)
            if(parcelable != null)
                diceImage.setImageBitmap(parcelable as Bitmap)
        }
    }

    fun reset() {
        diceImage.setImageResource(android.R.color.transparent)
        diceValue = -1
        diceValueLabel.visibility = View.INVISIBLE
    }
}
package com.carlosmartel.practica2.Fragments

import android.graphics.drawable.AnimationDrawable
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.content.res.ResourcesCompat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.carlosmartel.practica2.R
import kotlinx.android.synthetic.main.image_fragment.*
import kotlin.random.Random



class DiceImageFragment : Fragment(){
    private var diceImages :IntArray = intArrayOf(R.drawable.dice_1, R.drawable.dice_2, R.drawable.dice_3, R.drawable.dice_4, R.drawable.dice_5, R.drawable.dice_6)
    private val diceAnimation : AnimationDrawable = AnimationDrawable()
    private var diceValue: Int = -1
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.image_fragment, container, false)
    }


    fun getNumber():Int{
        diceImage.setImageResource(android.R.color.transparent)

        //todo put an animation
        for( i in 1 .. 10)
            diceAnimation.addFrame(ResourcesCompat.getDrawable(resources, diceImages[Random.nextInt(0,diceImages.size)], null)!!, 200)

        diceImage.background = ResourcesCompat.getDrawable(resources, android.R.color.transparent, null)

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

        outState.putInt("DiceValue", diceValue)
        outState.putInt("DiceImage", diceImages[diceValue-1])
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)

        if(savedInstanceState != null){
            diceValue = savedInstanceState.getInt("DiceValue")
            val diceImageId = savedInstanceState.getInt("DiceImage")
            println("DiceValue: $diceValue, DiceImage: $diceImageId")
            diceImage.setImageResource(diceImageId)
            diceValueLabel.text = getString(R.string.diceValueLabel, diceValue)
        }

    }
}
package com.carlosmartel.practica2.Fragments

import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.AnimationDrawable
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.os.Parcel
import android.os.Parcelable
import android.support.v4.app.Fragment
import android.support.v4.content.res.ResourcesCompat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.carlosmartel.practica2.CustomAnimationDrawable
import com.carlosmartel.practica2.CustomData
import com.carlosmartel.practica2.R
import kotlinx.android.synthetic.main.image_fragment.*
import kotlin.random.Random


class DiceImageFragment : Fragment(){
    private var diceImages :IntArray = intArrayOf(R.drawable.dice_1, R.drawable.dice_2, R.drawable.dice_3, R.drawable.dice_4, R.drawable.dice_5, R.drawable.dice_6)
    private var diceValue :Int = -1
    private var diceImageInterface : DiceImageListener? = null
    private var lastAnimationFrameValue = -1
    private var cad : CustomAnimationDrawable? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.image_fragment, container, false)

        view?.findViewById<TextView>(R.id.diceValueLabel)?.visibility = View.INVISIBLE

        return view
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        try{
            diceImageInterface = context as DiceImageListener
        } catch (e: ClassCastException){
            throw ClassCastException(context?.toString() + "must implement DiceImageListener")
        }
    }

    fun startDiceAnimation(){
        diceImage.setImageResource(android.R.color.transparent)

        val diceAnimation = AnimationDrawable()
        for(i in 1..10){
            lastAnimationFrameValue = Random.nextInt(0,diceImages.size)
            diceAnimation.addFrame(ResourcesCompat.getDrawable(resources, diceImages[lastAnimationFrameValue], null)!!, 200)
        }
        cad = object : CustomAnimationDrawable(diceAnimation) {
            override fun onAnimationStart() {
                diceImageInterface?.animationStarted()
            }

            override fun onAnimationFinish() {
                diceValue = getNumber()
                diceImageInterface?.animationEnded(diceValue)
            }
        }
        cad?.isOneShot = true

        diceImage.background = cad
        
        cad?.start()
    }

    fun getNumber():Int{
        diceValueLabel?.visibility = View.VISIBLE
        diceValue = lastAnimationFrameValue+1
        diceImage?.background = ResourcesCompat.getDrawable(resources, android.R.color.transparent, null)
        diceImage?.setImageResource(diceImages[lastAnimationFrameValue])
        diceValueLabel?.text = getString(R.string.diceValueLabel, diceValue)
        return diceValue
    }

    override fun onPause() {
        cad?.stop()
        super.onPause()
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
        cad?.start()
    }

    fun reset() {
        diceImage.setImageResource(android.R.color.transparent)
        diceValue = -1
        diceValueLabel.visibility = View.INVISIBLE
    }

    interface DiceImageListener{
        fun animationEnded(diceValue: Int)
        fun animationStarted()
    }
}
package com.carlosmartel.practica2

import android.graphics.drawable.AnimationDrawable
import android.os.Handler

abstract class CustomAnimationDrawable(animationDrawable: AnimationDrawable) : AnimationDrawable() {
    private val animationHandler = Handler()

    private val startRunnable = Runnable {
        onAnimationStart()
    }
    private val finishRunnable = Runnable {
        onAnimationFinish()
    }
    init {
        for(i in 0 until animationDrawable.numberOfFrames)
            this.addFrame(animationDrawable.getFrame(i), animationDrawable.getDuration(i))
    }


    override fun start() {
        super.start()
        animationHandler.post {
            onAnimationStart()
        }
        animationHandler.postDelayed({
            onAnimationFinish()
        }, getTotalDuration().toLong())
    }

    override fun stop() {
        super.stop()
        animationHandler.removeCallbacks(startRunnable)
        animationHandler.removeCallbacks(finishRunnable)
    }

    private fun getTotalDuration(): Int {
        var iDuration = 0
        for (i in 0 until this.numberOfFrames)
            iDuration += this.getDuration(i)
        return iDuration
    }

    abstract fun onAnimationFinish()

    abstract fun onAnimationStart()

}
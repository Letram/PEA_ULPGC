package com.carlosmartel.practica2

import android.content.res.Configuration
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.view.View
import com.carlosmartel.practica2.Fragments.DiceImageFragment
import com.carlosmartel.practica2.Fragments.PlayerScoreFragment
import com.carlosmartel.practica2.Fragments.PlayerTurnInfoFragment
import kotlin.random.Random

class GameActivity : AppCompatActivity(),
    PlayerTurnInfoFragment.PlayerTurnInfoInterface,
    DiceImageFragment.DiceImageListener{

    override var currentPlayer: Int = -1

    private var diceImageFragment : DiceImageFragment? = null
    private var playerScoreFragment : PlayerScoreFragment? = null
    private var playerTurnFragment : PlayerTurnInfoFragment? = null

    private var accPlayerValueInTurn : Int = 0
    private var scores = intArrayOf(0, 0)
    private var goal: Int = -1

    private var cpuTurn: Boolean = false
    private var cpuRolls: Int = -1
    private var singlePlayer = false

    override fun animationEnded(diceValue: Int){
        supportFragmentManager.beginTransaction().show(diceImageFragment!!).commit()
        accPlayerValueInTurn = playerTurnFragment!!.setAccValue(diceValue)
        playerTurnFragment!!.stopRolling()
        if(accPlayerValueInTurn == 0){
            collect(accPlayerValueInTurn)
        }
        else if (scores[currentPlayer-1] + accPlayerValueInTurn >= goal){
            collect(goal-scores[currentPlayer-1])
        }
        if(currentPlayer == 2){
            if(cpuRolls == 0){
                collect(accPlayerValueInTurn)
            }
            else{
                roll()
            }
        }
    }

    override fun roll(){
        cpuRolls--
        diceImageFragment?.startDiceAnimation()
        /*
        val diceValue = diceImageFragment!!.getNumber()
        supportFragmentManager.beginTransaction().show(diceImageFragment!!).commit()
        accPlayerValueInTurn = playerTurnFragment!!.setAccValue(diceValue)
        if(accPlayerValueInTurn == 0){
            collect(accPlayerValueInTurn)
            return false
        }
        else if (scores[currentPlayer-1] + accPlayerValueInTurn >= goal){
            collect(goal-scores[currentPlayer-1])
            return false
        }
        return true
        */
    }

    private fun rollCpu() : Boolean {
        val diceValue = diceImageFragment!!.getNumber()
        supportFragmentManager.beginTransaction().show(diceImageFragment!!).commit()
        accPlayerValueInTurn = playerTurnFragment!!.setAccValue(diceValue)
        if(accPlayerValueInTurn == 0){
            collect(accPlayerValueInTurn)
            return false
        }
        else if (scores[currentPlayer-1] + accPlayerValueInTurn >= goal){
            collect(goal-scores[currentPlayer-1])
            return false
        }
        return true
    }


    override fun collect(scoreValue: Int) {
        playerTurnFragment?.showBtns()
        cpuRolls = 0
        scores[currentPlayer-1] += scoreValue
        playerScoreFragment?.setScore(scores)
        if(scores[currentPlayer-1] == goal)
            openWinAlert()
        else{
            if(!singlePlayer && resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE)
                swapFragments()
            currentPlayer = if (currentPlayer == 1) 2 else 1
            playerTurnFragment?.resetAccScore()
            accPlayerValueInTurn = 0
            if(singlePlayer && currentPlayer == 2)enterCPUTurn()
        }
    }

    private fun enterCPUTurn() {
        playerTurnFragment?.hideBtns()
        cpuRolls= Random.nextInt(1,4)
        println("ENTER CPU TURN - Number of rolls: $cpuRolls")
        roll()
        /*
        for (index in 0 until rolls){
            if(!rollCpu()){
                playerTurnFragment?.showBtns()
                return
            }
            println(accPlayerValueInTurn)
        }
        playerTurnFragment?.showBtns()
        collect(accPlayerValueInTurn)
        */
    }


    private fun swapFragments() {

        val diceLayout = findViewById<View>(R.id.imageFragment).layoutParams
        val infoLayout = findViewById<View>(R.id.turnInfoFragment).layoutParams

        findViewById<View>(R.id.turnInfoFragment).layoutParams = diceLayout
        findViewById<View>(R.id.imageFragment).layoutParams = infoLayout
    }

    private fun openWinAlert() {
        val winner = when {
            intent!!.extras!!.getString(CustomData.EXTRA_GAMEMODE) == "multi" -> getString(R.string.player, currentPlayer)
            scores[0] != 20 -> getString(R.string.machine)
            else -> getString(R.string.player, currentPlayer)
        }
        val builder = AlertDialog.Builder(this)
        builder.setIcon(android.R.drawable.ic_dialog_alert)
            .setTitle(resources.getString(R.string.winTitle))
            .setMessage(getString(R.string.winDescription, winner))
            .setPositiveButton(resources.getString(R.string.winNewGame)
            ) { _, _ ->
                scores[1] = resources.getInteger(R.integer.initialScore)
                scores[0] = scores[1]
                currentPlayer = when {
                    (currentPlayer == 1 && !singlePlayer) -> 2
                    (currentPlayer == 2 && !singlePlayer) -> 1
                    else -> 1
                }

                playerTurnFragment?.resetAccScore()
                playerScoreFragment?.reset()
                diceImageFragment?.reset()
                accPlayerValueInTurn = 0
            }
            .setNegativeButton(resources.getString(R.string.winExit)
            ) { _, _ -> finish() }
            .setCancelable(false)
            .show()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game)

        currentPlayer = resources.getInteger(R.integer.currentPlayer)
        goal = resources.getInteger(R.integer.goal)
        singlePlayer = intent!!.extras!!.getString(CustomData.EXTRA_GAMEMODE) == "single"

        diceImageFragment = supportFragmentManager.findFragmentById(R.id.imageFragment) as DiceImageFragment

        playerScoreFragment = supportFragmentManager.findFragmentById(R.id.playerScoreFragment) as PlayerScoreFragment
        playerScoreFragment?.setup(intent?.extras?.getString(CustomData.EXTRA_GAMEMODE))
        playerTurnFragment = supportFragmentManager.findFragmentById(R.id.turnInfoFragment) as PlayerTurnInfoFragment


    }
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt(CustomData.CURRENT_PLAYER, currentPlayer)
        outState.putInt(CustomData.ACC_SCORE, accPlayerValueInTurn)
        outState.putIntArray(CustomData.SCORES, scores)
    }
    override fun onRestoreInstanceState(savedInstanceState: Bundle?) {
        super.onRestoreInstanceState(savedInstanceState)
        if(savedInstanceState != null){
            currentPlayer = savedInstanceState.getInt(CustomData.CURRENT_PLAYER)
            accPlayerValueInTurn = savedInstanceState.getInt(CustomData.ACC_SCORE)
            scores = savedInstanceState.getIntArray(CustomData.SCORES)!!
        }

    }
    override fun onBackPressed() {
        android.app.AlertDialog.Builder(this)
        .setIcon(android.R.drawable.ic_dialog_alert)
        .setTitle(resources.getString(R.string.alertTitle))
        .setMessage(resources.getString(R.string.alertQuestion))
        .setPositiveButton(resources.getString(R.string.alertYes)
        ) { _, _ ->
            finishAffinity()
        }
            .setNegativeButton(resources.getString(R.string.alertNo), null)
        .show()
        }
}

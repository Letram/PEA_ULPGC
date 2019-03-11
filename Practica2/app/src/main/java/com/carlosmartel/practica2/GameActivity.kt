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

    private var cpuRolls: Int = -1
    private var singlePlayer = false

    override fun animationStarted() {
        playerTurnFragment?.startRolling()
    }

    /**
     * @param diceValue: Int value of the last rolled dice
     *
     * This function is used to sum the value of the dice to the player's accumulated value in that turn.
     * In case of having reached the goal, then the points are collected automatically.
     */
    override fun animationEnded(diceValue: Int){
        accPlayerValueInTurn = playerTurnFragment!!.setAccValue(diceValue)
        playerTurnFragment!!.stopRolling()
        if(accPlayerValueInTurn == 0){
            collect(accPlayerValueInTurn)
        }
        else if (scores[currentPlayer-1] + accPlayerValueInTurn >= goal){
            collect(goal-scores[currentPlayer-1])
        }
        if(currentPlayer == 2 && singlePlayer){
            if(cpuRolls == 0){
                collect(accPlayerValueInTurn)
            }
            else{
                roll()
            }
        }
    }

    /**
     *
     */
    override fun roll(){
        cpuRolls--
        diceImageFragment?.startDiceAnimation()
    }

    /**
     * Collects the accumulated points of the current player turn and updates the view.
     * In case of having reached the goal, the game is over.
     *
     * @param scoreValue: Int score accumulated in the current turn.
     */
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
            playerTurnFragment?.resetAccScore(currentPlayer)
            accPlayerValueInTurn = 0
            if(singlePlayer && currentPlayer == 2)enterCPUTurn()
        }
    }

    private fun enterCPUTurn() {
        playerTurnFragment?.cpuTurn()
        cpuRolls= Random.nextInt(1,4)
        println("ENTER CPU TURN - Number of rolls: $cpuRolls")
        roll()
    }


    private fun swapFragments() {

        val diceLayout = findViewById<View>(R.id.imageFragment).layoutParams
        val infoLayout = findViewById<View>(R.id.turnInfoFragment).layoutParams

        findViewById<View>(R.id.turnInfoFragment).layoutParams = diceLayout
        findViewById<View>(R.id.imageFragment).layoutParams = infoLayout
    }

    /**
     * Opens an alert when a player wins (any player or CPU, doesn't matter). That alert cannot be dismissed using the back button
     */
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

                playerTurnFragment?.resetAccScore(currentPlayer)
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
        if(savedInstanceState != null){
            currentPlayer = savedInstanceState.getInt(CustomData.CURRENT_PLAYER)
            accPlayerValueInTurn = savedInstanceState.getInt(CustomData.ACC_SCORE)
            scores = savedInstanceState.getIntArray(CustomData.SCORES)!!
        }
        super.onRestoreInstanceState(savedInstanceState)
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

package com.example.tictactoe

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.core.view.get
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    var winner:Int = 0
    var player1:Boolean = true;
    var board: Array<Int> = Array(9){ _: Int -> 0 }
    var nMoves: Int = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    fun dropInView(view:View){
        if(winner != 0 || nMoves == 9) {
            var s = if(player1) "Yellow won last game\n" else if(winner == 2) "Red won last game\n" else "Previous game was draw\n"
            Toast.makeText(this, "Game is already Over\n"+ s + "Reset to start again...", Toast.LENGTH_LONG).show()
            return;
        }
        val pos = view.tag.toString().toInt()
        if(board[pos] == 0){
            val image: ImageView = view as ImageView;
            image.translationY = -1500F
            image.setImageResource( if(this.player1) R.drawable.yellow else R.drawable.red)
            image.animate().translationYBy(1500F).duration = 300;
            board[pos] = if(player1) 1 else 2
            nMoves++
            if(this.checkBoard() && winner != 0){
                if(winner!=0) Toast.makeText(this, "Game Over\n"+ if(player1) "Yellow wins" else "Red wins", Toast.LENGTH_LONG).show()
                else if(nMoves == 9) Toast.makeText(this, "Game Over\nThis game is draw...", Toast.LENGTH_LONG).show()
            }
            player1 = !player1;
            this.updateStatus()
        }else{
            Toast.makeText(this, "The position is already taken...\nChoose another one", Toast.LENGTH_SHORT).show()
        }
    }

    private fun checkBoard():Boolean{
        // Row wise checking
        for (i in 0..2){
            if(board[3*i+0] == board[3*i+1] && board[3*i+1] == board[3*i+2]) {
                if(board[3*i+0] == 1){ winner = 1; return true;}
                else if(board[3*i+0]==2) {winner = 2; return true;}
            }
        }
        // Column wise checking
        for (i in 0..2){
            if(board[0+i] == board[3+i] && board[3+i] == board[6+i]) {
                if(board[0+i] == 1){ winner = 1; return true;}
                else if(board[0+i]==2) {winner = 2; return true;}
            }
        }


        // Diagonal Check
        if(board[0] == board[4] && board[4] == board[8]) {
            if(board[0] == 1) { winner = 1; return true;}
            else if(board[0]==2) { winner = 2; return true;}
        }
        if(board[2] == board[4] && board[4] == board[6]) {
            if(board[2] == 1) { winner = 1; return true;}
            else if(board[2]==2) { winner = 2; return true;}
        }
        return nMoves == 9;
    }

    fun gameReset(view: View){
        lateinit var image : ImageView
        for(i in 0..8) {
            image = gridLayout[i] as ImageView
            image.setImageResource(R.color.transparent)
            board[i] = 0
        }
        this.player1 = true
        this.nMoves = 0
        this.winner = 0
        this.updateStatus()
    }

    private fun updateStatus(){
        match_status.text = if(winner == 1) "Yellow Wins"
                            else if (winner == 2) "Red Wins"
                            else if (winner == 0 && nMoves == 9) "No one wins"
                            else if (player1) "Turn of Yellow"
                            else "Turn of Red"
    }


}
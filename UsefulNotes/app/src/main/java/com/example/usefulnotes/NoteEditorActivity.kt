package com.example.usefulnotes

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_note_editor.*

class NoteEditorActivity : AppCompatActivity() {
    private var noteId:Int = -1
    private var noteChanged:Boolean = false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_note_editor)
        this.initializeIntent()
        this.initializeActivity()
    }

    private fun initializeIntent(){
        intent.putExtra("@INIT", "")
        noteId = intent.getIntExtra("noteId", -1)

        if(noteId == -1){
            MainActivity.notes.add("")
            MainActivity.notifyDataSetChanged()
            noteId = MainActivity.notes.size-1
            editText_note.setText("")
        }else{
            editText_note.setText(MainActivity.notes[noteId])
        }
    }

    private fun initializeActivity(){
        editText_note.addTextChangedListener(object:TextWatcher{
            override fun afterTextChanged(p0: Editable?) {}

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if(noteId >-1) {
                    noteChanged = true
                    MainActivity.notes[noteId] = p0.toString()
                }
            }
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

        })
    }


    // Other Life Cycle Methods
    override fun onDestroy() {
        super.onDestroy()
        if(noteChanged){
            MainActivity.notifyDataSetChanged()
            val sharedPreferences = getSharedPreferences("com.example.usefulnotes", Context.MODE_PRIVATE)
            val set:HashSet<String> = HashSet(MainActivity.notes)
            sharedPreferences.edit().putStringSet("notes", set).apply()
            print(set)

        }
    }

}
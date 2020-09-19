package com.example.usefulnotes

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    companion object{
        var notes = ArrayList<String>()
        private lateinit var adapter:ArrayAdapter<String>
        fun notifyDataSetChanged() { adapter.notifyDataSetChanged()}
    }

    private lateinit var sharedPreferences:SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        this.initializeIntent()
        this.initializeActivity()


    }

    private fun initializeIntent(){
        intent.putExtra("@INIT", "")
        sharedPreferences = getSharedPreferences("com.example.usefulnotes", Context.MODE_PRIVATE)
        val hashSet = sharedPreferences.getStringSet("notes", HashSet<String>())
        if(!hashSet.isNullOrEmpty()){
            notes = ArrayList(hashSet)
        }
        adapter = ArrayAdapter<String>(this, android.R.layout.simple_expandable_list_item_1, notes)
        listView_notes.adapter = adapter
    }

    private fun initializeActivity(){
        listView_notes.onItemClickListener = object : AdapterView.OnItemClickListener{
            override fun onItemClick(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                var nextIntent:Intent = Intent(applicationContext, NoteEditorActivity::class.java)
                nextIntent.putExtra("noteId", p2)
                startActivity(nextIntent)
            }
        }

        listView_notes.onItemLongClickListener = AdapterView.OnItemLongClickListener { p0, p1, index, p3 ->
                val alert = AlertDialog.Builder(this@MainActivity)
                alert.setIcon(android.R.drawable.ic_dialog_alert)
                    .setTitle("Are you sure?")
                    .setMessage("Do want to delete this note?")
                    .setPositiveButton("Yes") { p0, p1 ->
                        notes.removeAt(index)
                        adapter.notifyDataSetChanged()
                        val set:HashSet<String> = HashSet(MainActivity.notes)
                        sharedPreferences.edit().putStringSet("notes", set).apply()
                    }
                    .setNegativeButton("No", null)
                alert.show()
                true
            }
    }

    // Option Menu Code
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val menuInflater = MenuInflater(this)
        menuInflater.inflate(R.menu.option_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        super.onOptionsItemSelected(item)
        if(item.itemId == R.id.optionMenuItem_addNote){
            val nextIntent = Intent(applicationContext, NoteEditorActivity::class.java)
            startActivity(nextIntent)
        }

        return false
    }

}
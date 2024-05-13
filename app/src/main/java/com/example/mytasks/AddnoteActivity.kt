package com.example.mytasks

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.mytasks.databinding.ActivityAddnoteBinding

class AddnoteActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAddnoteBinding
    private lateinit var db: NoteDatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddnoteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        db = NoteDatabaseHelper(this)
        binding.saveButton.setOnClickListener {
            val title = binding.titleEditText.text.toString()
            val content = binding.contextEditText.text.toString()
            if (title.isNotEmpty() && content.isNotEmpty()) {
                val note = Note(0, title, content)
                db.insertNote(note)
                Toast.makeText(this, "Note saved", Toast.LENGTH_SHORT).show()
                finish()
            } else {
                Toast.makeText(this, "Title or content cannot be empty", Toast.LENGTH_SHORT).show()
            }
        }

    }
}

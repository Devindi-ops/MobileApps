package com.example.mytasks

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log

class NoteDatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {
    companion object {
        private const val DATABASE_NAME = "mynotesapp.db"
        private const val DATABASE_VERSION = 1
        private const val TABLE_NAME = "myallnotes"
        private const val COLUMN_ID = "id"
        private const val COLUMN_TITLE = "title"
        private const val COLUMN_CONTENT = "content"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        val createTableQuery = "CREATE TABLE $TABLE_NAME($COLUMN_ID INTEGER PRIMARY KEY, $COLUMN_TITLE TEXT, $COLUMN_CONTENT TEXT)"
        db?.execSQL(createTableQuery)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(db)
    }

    fun insertNote(note: Note) {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_TITLE, note.title)
            put(COLUMN_CONTENT, note.content)
        }
        db.insert(TABLE_NAME, null, values)
        db.close()
    }
    fun getAllNotes(): List<Note> {
        val notes = mutableListOf<Note>()
        val db = readableDatabase
        val cursor = db.query(TABLE_NAME, null, null, null, null, null, null)
        if (cursor.moveToFirst()) {
            do {
                val id = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID))
                val title = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TITLE))
                val content = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_CONTENT))
                notes.add(Note(id, title, content))
            } while (cursor.moveToNext())
        }
        cursor.close()
        db.close()
        Log.d("Database", "Fetched notes: ${notes.size}")
        return notes
    }

    fun updateNote(note: Note): Boolean {
        val db = writableDatabase
        val contentValues = ContentValues()
        contentValues.put(COLUMN_TITLE, note.title)
        contentValues.put(COLUMN_CONTENT, note.content)

        val success = db.update(TABLE_NAME, contentValues, "$COLUMN_ID=?", arrayOf(note.id.toString()))
        db.close()
        return success > 0
    }

    fun getNoteByID(noteID:Int):Note{
        val db=readableDatabase
        val query="SELECT * FROM $TABLE_NAME WHERE $COLUMN_ID=$noteID"
        val cursor=db.rawQuery(query,null)
        cursor.moveToFirst()


        val id=cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID))
        val title=cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TITLE))
        val content=cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_CONTENT))


        cursor.close()
        db.close()
        return Note(id,title, content)

    }
    fun deleteNote(noteId:Int){
        val db=writableDatabase
        val whereClause="$COLUMN_ID=?"
        val whereArgs=arrayOf(noteId.toString())
        db.delete(TABLE_NAME,whereClause,whereArgs)
        db.close()
    }

}

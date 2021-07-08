package com.example.myplanner.db

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.provider.BaseColumns
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class DbManager(context: Context) {
    val dbHelper = DbHelper(context)
    var db: SQLiteDatabase? = null
    fun openDb() {
        db = dbHelper.writableDatabase
    }

    fun insertDb(title: String, point: String, dateCreate: String) {
        val values = ContentValues().apply {
            put(DbName.COLUMN_NAME_TITLE, title)
            put(DbName.COLUMN_NAME_POINT, point)
            put(DbName.COLUMN_NAME_DATE_CREATE, dateCreate)
        }
        db?.insert(DbName.TABLE_NAME, null, values)
    }

    suspend fun readDb(): ArrayList<ListItem> = withContext(Dispatchers.IO){
        val dList = ArrayList<ListItem>()
        val cursor = db?.query(DbName.TABLE_NAME, null, null, null, null, null, null)
        while (cursor?.moveToNext()!!) {
            val textId = cursor.getInt(cursor.getColumnIndex(BaseColumns._ID))
            val textTitle = cursor.getString(cursor.getColumnIndex(DbName.COLUMN_NAME_TITLE))
            val textPoint = cursor.getString(cursor.getColumnIndex(DbName.COLUMN_NAME_POINT))
            val textDateCreate = cursor.getString(cursor.getColumnIndex(DbName.COLUMN_NAME_DATE_CREATE))
            var item = ListItem()
            item.id = textId
            item.title = textTitle
            item.point = textPoint
            item.date_create = textDateCreate
            dList.add(item)
        }
        cursor.close()
        return@withContext dList
    }

   fun removeItem (id : String){
       var selection = BaseColumns._ID + "=$id"
       db?.delete(DbName.TABLE_NAME, selection, null)
   }

   fun updateItem (id : Int, title: String, point: String, dateCreate: String){
       val selection = BaseColumns._ID + "=$id"
       val values = ContentValues().apply {
           put(DbName.COLUMN_NAME_TITLE, title)
           put(DbName.COLUMN_NAME_POINT, point)
           put(DbName.COLUMN_NAME_DATE_CREATE, dateCreate)
       }
       db?.update(DbName.TABLE_NAME, values, selection, null)
   }

    fun closeDb(){
        dbHelper.close()
    }
}

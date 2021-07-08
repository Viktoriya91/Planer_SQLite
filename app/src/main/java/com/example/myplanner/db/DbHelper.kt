package com.example.myplanner.db

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DbHelper (context: Context):SQLiteOpenHelper(context, DbName.DB_NAME, null, DbName.DB_VERSION){
    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL(DbName.CREATE_TABLE)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL(DbName.SQL_DELETE_TABLE)
        onCreate(db)
    }
}
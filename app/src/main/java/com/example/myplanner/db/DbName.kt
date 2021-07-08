package com.example.myplanner.db

import android.provider.BaseColumns

object DbName {
    const val DB_VERSION = 1
    const val DB_NAME = "DbPlanner.db"
    const val TABLE_NAME = "daily_planner"
    const val COLUMN_NAME_TITLE = "title"
    const val COLUMN_NAME_POINT = "point"
    const val COLUMN_NAME_DATE_CREATE = "date_create"
    const val CREATE_TABLE = "CREATE TABLE IF NOT EXISTS $TABLE_NAME(" +
            "${BaseColumns._ID} INTEGER PRIMARY KEY," +
            "$COLUMN_NAME_TITLE TEXT, $COLUMN_NAME_POINT TEXT, $COLUMN_NAME_DATE_CREATE TEXT)"
    const val SQL_DELETE_TABLE = "DROP TABLE IF EXISTS $TABLE_NAME"
}
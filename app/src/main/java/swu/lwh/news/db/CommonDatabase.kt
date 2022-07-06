package swu.lwh.news.db

import android.content.Context
import android.database.sqlite.SQLiteDatabase


class CommonDatabase(context: Context?) {
    private val dbHelper: DBHelper by lazy { DBHelper(context) }
    fun getWrite(): SQLiteDatabase {
        return dbHelper.writableDatabase
    }

    fun getRead(): SQLiteDatabase {
        return dbHelper.readableDatabase
    }
}
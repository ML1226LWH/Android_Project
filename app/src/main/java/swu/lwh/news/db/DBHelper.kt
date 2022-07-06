package swu.lwh.news.db

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DBHelper(context: Context?) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {
    companion object {
        const val DATABASE_NAME = "MyNews.db"
        const val DATABASE_VERSION = 1

        const val ID = "_id"

        const val TABLE_USER = "user"

        const val NAME = "name"
        const val PASSWORD = "password"
        const val SEX = "sex"
        const val INFO = "info"

        const val TABLE_NEWS = "news"

        const val TITLE = "title"
        const val DATE = "date"
        const val AUTHOR_NAME = "author_name"
        const val PIC_S = "thumbnail_pic_s"
        const val PIC_S02 = "thumbnail_pic_s02"
        const val PIC_S03 = "thumbnail_pic_s03"
        const val URL = "url"
    }


    override fun onCreate(db: SQLiteDatabase) {
        //创建用户表
        val sqUser =
            "CREATE TABLE $TABLE_USER ( $ID INTEGER PRIMARY KEY AUTOINCREMENT, $NAME String, $PASSWORD String, $SEX String, $INFO String);"
        db.execSQL(sqUser)
        val sqNews =
            "CREATE TABLE $TABLE_NEWS ( $ID INTEGER PRIMARY KEY AUTOINCREMENT, $TITLE String, $DATE String, $AUTHOR_NAME String, $PIC_S String, $PIC_S02 String, $PIC_S03 String, $URL String);"
        db.execSQL(sqNews)

        //创建默认用户
        val contentValues = ContentValues()
        contentValues.put(NAME, "admin")
        contentValues.put(PASSWORD, "123456")
        db.insert(TABLE_USER, null, contentValues)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
    }
}
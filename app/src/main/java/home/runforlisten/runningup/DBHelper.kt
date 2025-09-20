package home.runforlisten.runningup

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log

//내장 DB 클래스
class DBHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "runningup.db" // 데이터베이스 이름
        private const val DATABASE_VERSION = 1 // 데이터베이스 버전
        private const val TABLE_NAME = "run_history" // 테이블 이름
    }



    override fun onCreate(db: SQLiteDatabase) {
        val createTable = """
            CREATE TABLE IF NOT EXISTS $TABLE_NAME(
            
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                pace TEXT,
                memo TEXT
            
            )
            
        """.trimIndent()
        db.execSQL(createTable)
    }

    override fun onUpgrade(db: SQLiteDatabase, p1: Int, p2: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(db)
    }


    fun insertPaceTest(pace: String) : Long{
        val db = writableDatabase
        val values = ContentValues().apply {
            put("pace", pace)
        }

        return db.insert("run_history", null, values)
    }

    fun selectPaceTest() : String{
        val db = readableDatabase
        val cursor = db.rawQuery("SELECT pace  FROM $TABLE_NAME ORDER BY id DESC LIMIT 1", null)

        var resultPace = ""


        if (cursor.moveToFirst()) {
                resultPace = cursor.getString(0) // TEXT 컬럼 값
        }


        cursor.close()
        db.close()

        return resultPace

    }


    fun insertTest(memo: String) : Long{
        val db = writableDatabase
        Log.d("memo test", memo)
        val values = ContentValues().apply {
            put("memo", memo)

        }

        return db.insert("run_history", null, values)


    }


}
package swu.lwh.news

import android.app.Application
import android.content.Context

class MyApplication : Application() {
    companion object{
        lateinit var context:Context
        val KEY="75d585d17da54c3b6f8cfe7d9574b867"
        //备用 "65d4c89f2460e131bd8b288f3f70bff6" "75d585d17da54c3b6f8cfe7d9574b867"
    }

    override fun onCreate() {
        super.onCreate()
        context=baseContext
    }
}
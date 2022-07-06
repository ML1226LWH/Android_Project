package swu.lwh.news.http

import com.google.gson.Gson
import okhttp3.Call
import okhttp3.OkHttpClient
import okhttp3.Request
import java.util.concurrent.TimeUnit
import kotlin.concurrent.thread

class HiOkHttp{//不用构建实例对象  想让整个程序运行时作为全局变量，只有一份

    val client:OkHttpClient = OkHttpClient.Builder()
        .connectTimeout(10,TimeUnit.SECONDS)
        .readTimeout(10,TimeUnit.SECONDS)
        .writeTimeout(10,TimeUnit.SECONDS)
        .build()

    fun get(url:String) {
        thread {  //构造请求体
            val request: Request = Request.Builder()
                .url(url)
                .build()
            //构造请求对象
            val call: Call = client.newCall(request)
            //发起同步请求execute--同步执行，100ms
            val response = call.execute()
            val json = response.body?.string()
            println("get reponse:")
        }
    }
}
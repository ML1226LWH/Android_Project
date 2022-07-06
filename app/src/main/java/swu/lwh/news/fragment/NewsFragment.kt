package swu.lwh.news.fragment

import android.content.ContentValues
import android.database.SQLException
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.gson.Gson
import okhttp3.OkHttpClient
import okhttp3.Request
import swu.lwh.news.MyApplication
import swu.lwh.news.Util.RecyclerUtils
import swu.lwh.news.adapter.NewsAdapter
import swu.lwh.news.databinding.FragmentNewsBinding
import swu.lwh.news.db.CommonDatabase
import swu.lwh.news.db.DBHelper
import swu.lwh.news.model.NewsResponse
import kotlin.concurrent.thread

class NewsFragment : Fragment() {
    companion object {
        @JvmStatic
        fun newInstance(newsType: String) = NewsFragment().apply {
            arguments = Bundle().apply {
                putString("newsType", newsType)
            }
        }
    }

    val database: CommonDatabase by lazy { CommonDatabase(activity) }
    private val bind: FragmentNewsBinding by lazy { FragmentNewsBinding.inflate(layoutInflater) }
    private val adapter = NewsAdapter()
    private var newsType = ""

    private fun refresh() {
        thread {
            System.out.println(newsType)
            val request = Request.Builder()
                .url("http://v.juhe.cn/toutiao/index?type=" + newsType + "&page=&page_size=&is_filter=&key=" + MyApplication.KEY)
                .build()
            val response = OkHttpClient().newCall(request).execute()//发送请求，返回的数据保存在response里
            val json = response.body?.string()
            val newsResponse = Gson().fromJson(json, NewsResponse::class.java)
            newsResponse?.result?.apply { activity?.runOnUiThread { adapter.setList(data) } }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        arguments?.let {
            newsType = it.getString("newsType", "")
        }
        RecyclerUtils.initList(activity, bind.recycler)
        bind.recycler.adapter = adapter
        refresh()
        bind.newsFresh.setColorSchemeColors(Color.RED)
        bind.newsFresh.setOnRefreshListener {
            thread {
                Thread.sleep(700)
                activity?.runOnUiThread {
                    refresh()
                    bind.newsFresh.isRefreshing = false
                }
            }
        }
        adapter.collection = {
            try {
                //获取数据库操作权限
                val db = database.getWrite()
                //使用ContentValues类对数据进行封装
                val contentValues = ContentValues()
                contentValues.put(DBHelper.TITLE, it.title)
                contentValues.put(DBHelper.DATE, it.date)
                contentValues.put(DBHelper.AUTHOR_NAME, it.author_name)
                contentValues.put(DBHelper.PIC_S, it.thumbnail_pic_s)
                contentValues.put(DBHelper.PIC_S02, it.thumbnail_pic_s02)
                contentValues.put(DBHelper.PIC_S03, it.thumbnail_pic_s03)
                contentValues.put(DBHelper.URL, it.url)
                //使用insert语句进行插入操作
                db.insert(DBHelper.TABLE_NEWS, null, contentValues)
                //创建对话框提示用户注册成功
                Toast.makeText(activity, "新闻已收藏", Toast.LENGTH_SHORT).show()
            } catch (e: SQLException) {
                e.message
            }
        }
        return bind.root
    }
}
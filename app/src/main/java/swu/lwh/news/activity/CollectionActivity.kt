package swu.lwh.news.activity

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import swu.lwh.news.Util.RecyclerUtils
import swu.lwh.news.adapter.NewsAdapter
import swu.lwh.news.databinding.ActivityCollectionBinding
import swu.lwh.news.db.CommonDatabase
import swu.lwh.news.db.DBHelper
import swu.lwh.news.model.News

class CollectionActivity : AppCompatActivity() {
    val database: CommonDatabase by lazy { CommonDatabase(this) }
    val bind: ActivityCollectionBinding by lazy { ActivityCollectionBinding.inflate(layoutInflater) }
    val adapter = NewsAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(bind.root)

        RecyclerUtils.initList(this, bind.recycler)
        bind.recycler.adapter = adapter
        initData()
        adapter.collection = {
            val db = database.getWrite()
            db.delete(DBHelper.TABLE_NEWS, "${DBHelper.URL} = '${it.url}'", null)
            Toast.makeText(this, "已取消收藏", Toast.LENGTH_LONG).show()
            adapter.remove(it)
        }
    }

    //从数据库加载数据
    @SuppressLint("Range")//忽略警告
    private fun initData() {
        val db = database.getRead()
        val cursor = db.rawQuery("select * from ${DBHelper.TABLE_NEWS}", null)
        val blankList = ArrayList<News>()
        while (cursor.moveToNext()) {
            blankList.add(
                News(
                    cursor.getString(cursor.getColumnIndex(DBHelper.TITLE)),
                    cursor.getString(cursor.getColumnIndex(DBHelper.DATE)),
                    cursor.getString(cursor.getColumnIndex(DBHelper.AUTHOR_NAME)),
                    cursor.getString(cursor.getColumnIndex(DBHelper.PIC_S)),
                    cursor.getString(cursor.getColumnIndex(DBHelper.PIC_S02)),
                    cursor.getString(cursor.getColumnIndex(DBHelper.PIC_S03)),
                    cursor.getString(cursor.getColumnIndex(DBHelper.URL))
                )
            )
        }
        cursor.close()
        adapter.setList(blankList)
    }
}
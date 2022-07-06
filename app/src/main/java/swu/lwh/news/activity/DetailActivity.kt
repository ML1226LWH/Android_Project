package swu.lwh.news.activity

import android.os.Bundle
import android.os.PersistableBundle
import android.view.Menu
import android.view.MenuItem
import android.webkit.WebView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import kotlinx.android.synthetic.main.activity_detail.*
import swu.lwh.news.R

class DetailActivity:AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        // 设置标题栏
        setSupportActionBar(detail_tool_bar)

        // 去掉默认的标题
        title = ""
        // 设置标题的居中文字
        detail_real_title.text = intent.getStringExtra("news_from=")
        // 设置系统自带的home按钮(小箭头)可见
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        // 打开新闻网页
        val url = intent.getStringExtra("url=")
        if (url != null) {
            news_web_view.loadUrl(url)
        }
    }

    // 给Activity加载标题栏的菜单项
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        //写一个menu的资源文件.然后创建就行了.
        menuInflater.inflate(R.menu.detail_tool_bar_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    //给系统自带的home按钮(小箭头)添加点击事件：销毁本页面返回上一级
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
        }
        return super.onOptionsItemSelected(item)
    }

}
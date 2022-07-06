package swu.lwh.news.activity

import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import swu.lwh.news.R
import swu.lwh.news.Util.showToast

class SearchActivity :AppCompatActivity(){
    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onCreate(savedInstanceState, persistentState)
        setContentView(R.layout.activity_search)
        System.out.println("Search")
        val searchCancelButton=findViewById<TextView>(R.id.home_edit_text)
        searchCancelButton.setOnClickListener { finish() }
        val searchEditText=findViewById<EditText>(R.id.home_edit_text)
        searchEditText.setOnEditorActionListener{
                _, keyCode, _ ->
            // 如果点击了回车键，即搜索键，就弹出一个toast
            if (keyCode == EditorInfo.IME_ACTION_SEARCH) {
                "你输入了${searchEditText.text}".showToast()
                true
            } else {
                false
            }
        }
    }
}
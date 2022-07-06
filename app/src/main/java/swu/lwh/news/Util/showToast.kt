package swu.lwh.news.Util

import android.widget.Toast
import swu.lwh.news.MyApplication

fun String.showToast() {
    Toast.makeText(MyApplication.context,this,Toast.LENGTH_SHORT).show()
}
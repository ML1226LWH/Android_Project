package swu.lwh.news.activity

import android.content.ContentValues
import android.database.SQLException
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import swu.lwh.news.databinding.ActivityRegisterBinding
import swu.lwh.news.db.CommonDatabase
import swu.lwh.news.db.DBHelper

/**
 * 新用户注册
 */
class RegisterActivity : AppCompatActivity() {
    val database: CommonDatabase by lazy { CommonDatabase(this) }
    val bind: ActivityRegisterBinding by lazy { ActivityRegisterBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(bind.root)
        bind.btnRegister.setOnClickListener { v ->
            val name = bind.editName.getText().toString()
            val password = bind.editPassword.getText().toString()
            val passwordSure = bind.editPasswordSure.getText().toString()
            if (name.isEmpty() || password.isEmpty() || passwordSure.isEmpty()) {
                Toast.makeText(this, "输入信息不能为空", Toast.LENGTH_SHORT).show()
            } else if (password == passwordSure) {
                register()
            } else {
                Toast.makeText(this, "确认密码不一致", Toast.LENGTH_SHORT).show()
            }
        }
        bind.btnClose.setOnClickListener { v -> finish() }
    }

    private fun register() {
        val name = bind.editName.getText().toString()
        val password = bind.editPassword.getText().toString()
        val sex = bind.spinnerSex.getSelectedItem().toString()
        val info = bind.spinner.getSelectedItem().toString()
        try {
            //获取数据库操作权限
            val db = database.getWrite()
            //使用ContentValues类对数据进行封装
            val contentValues = ContentValues()
            contentValues.put(DBHelper.NAME, name)
            contentValues.put(DBHelper.PASSWORD, password)
            contentValues.put(DBHelper.SEX, sex)
            contentValues.put(DBHelper.INFO, info)
            //使用insert语句进行插入操作
            db.insert(DBHelper.TABLE_USER, null, contentValues)
            //创建对话框提示用户注册成功
            Toast.makeText(this@RegisterActivity, "注册成功", Toast.LENGTH_SHORT).show()
            finish()
        } catch (e: SQLException) {
            //创建对话框提示用户注册失败
            Toast.makeText(this@RegisterActivity, "注册失败", Toast.LENGTH_SHORT).show()
            e.message
        }
    }
}
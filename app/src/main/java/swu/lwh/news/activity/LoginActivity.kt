package swu.lwh.news.activity

import android.content.Intent
import android.database.SQLException
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import swu.lwh.news.databinding.ActivityLoginBinding
import swu.lwh.news.db.CommonDatabase
import swu.lwh.news.db.DBHelper
import swu.lwh.news.fragment.PersonFragment

/**
 * 登录
 */
class LoginActivity : AppCompatActivity() {
    val database: CommonDatabase by lazy { CommonDatabase(this) }
    val loginBinding: ActivityLoginBinding by lazy { ActivityLoginBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(loginBinding.root)
        loginBinding.btnLogin.setOnClickListener { v ->
            if (loginBinding.editName.text.toString()
                    .isEmpty() || loginBinding.editPassword.text.toString().isEmpty()
            ) {
                Toast.makeText(this, "账号密码输入错误", Toast.LENGTH_SHORT).show()
            } else if (isRegister()) {
                PersonFragment.isLogin = true
                PersonFragment.userName = loginBinding.editName.text.toString()
                finish()
            } else {
                Toast.makeText(this, "账号密码错误", Toast.LENGTH_SHORT).show()
            }
        }
        loginBinding.btnRegister.setOnClickListener { v ->
            startActivity(Intent(this, RegisterActivity::class.java))
        }
    }


    /**
     * 判断用户是否注册或输入密码是否正确
     */
    private fun isRegister(): Boolean {
        return try {
            val db = database.getRead()
            //查询是否有相同的用户名和密码
            val cursor = db.query(
                DBHelper.TABLE_USER, arrayOf(DBHelper.NAME, DBHelper.PASSWORD),
                "${DBHelper.NAME}='${
                    loginBinding.editName.text.toString().trim()
                }' AND ${DBHelper.PASSWORD}='${
                    loginBinding.editPassword.text.toString().trim()
                }'",
                null, null, null, null
            )
            //获取集合的数据数量
            val Counts = cursor.count
            cursor.close()
            //如果集合为空，则说明没有相同的用户名和密码，返回false
            Counts > 0
        } catch (e: SQLException) {
            e.message
            false
        }
    }
}
package swu.lwh.news.fragment

import android.annotation.SuppressLint
import android.content.Intent
import android.database.SQLException
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import swu.lwh.news.Util.ConfigData
import swu.lwh.news.Util.showToast
import swu.lwh.news.activity.CollectionActivity
import swu.lwh.news.activity.LoginActivity
import swu.lwh.news.databinding.FragmentPersonBinding
import swu.lwh.news.databinding.LayoutModifyBinding
import swu.lwh.news.db.CommonDatabase
import swu.lwh.news.db.DBHelper


class PersonFragment : Fragment() {
    companion object {
        var isLogin = false //记录登录状态
        var userName = "未登录" //记录登录用户名
    }

    val database: CommonDatabase by lazy { CommonDatabase(activity) }
    val bind: FragmentPersonBinding by lazy { FragmentPersonBinding.inflate(layoutInflater) }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val isNight: Boolean = ConfigData.getIsNight()
        //设置白夜间模式
        bind.switchNight.isChecked = isNight
        bind.switchNight.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                ConfigData.setIsNight(true)
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                ConfigData.setIsNight(false)
            }
            activity?.recreate()
        }
        //账号安全
        bind.layoutMePra.setOnClickListener {
            if (isLogin) {
                editInfo()
            } else {
                startActivity(Intent(activity, LoginActivity::class.java))
            }
        }
        //关注
        bind.layoutMeRedBag.setOnClickListener {
            if (isLogin) {
                "关注".showToast()
            } else {
                startActivity(Intent(activity, LoginActivity::class.java))
            }
        }
        //消息通知
        bind.layoutMeWordList.setOnClickListener {
            if (isLogin) {
                "消息通知".showToast()
            } else {
                startActivity(Intent(activity, LoginActivity::class.java))
            }
        }
        //隐私
        bind.layoutPrivacy.setOnClickListener {
            if (isLogin) {
                "隐私".showToast()
            } else {
                startActivity(Intent(activity, LoginActivity::class.java))
            }
        }
        //意见反馈
        bind.layoutOpinion.setOnClickListener {
            if (isLogin) {
                "意见".showToast()
            } else {
                startActivity(Intent(activity, LoginActivity::class.java))
            }
        }
        //关于
        bind.layoutMeAbout.setOnClickListener {
            if (isLogin) {
                "关于".showToast()
            } else {
                startActivity(Intent(activity, LoginActivity::class.java))
            }
        }
        //收藏
        bind.layoutMeOrder.setOnClickListener {
            if (isLogin) {
                startActivity(Intent(activity, CollectionActivity::class.java))
            } else {
                startActivity(Intent(activity, LoginActivity::class.java))
            }
        }
        bind.loginUserInfo.setOnClickListener {
            if (!isLogin) {
                startActivity(Intent(activity, LoginActivity::class.java))
            }
        }

        bind.tvLogin.setOnClickListener { v ->
            if (isLogin) {
                activity?.let {
                    AlertDialog.Builder(it)
                        .setTitle("温馨提示")
                        .setMessage("确定要退出登录吗？")
                        .setPositiveButton("确认") { dialog, _ ->
                            dialog.dismiss()
                            isLogin = false
                            userName = "未登录"
                            setText()
                            startActivity(Intent(activity, LoginActivity::class.java))
                        }
                        .setNegativeButton("取消") { dialog, _ -> dialog.dismiss() }
                        .show()
                }
            } else {
                startActivity(Intent(activity, LoginActivity::class.java))
            }
        }
        return bind.root
    }

    override fun onResume() {
        super.onResume()
        setText()
    }

    private fun setText() {
        bind.textMeWords.text = userName
        bind.tvLogin.text = if (isLogin) "退出登录" else "登录"
    }

    @SuppressLint("Range")
    private fun editInfo() {
        val modify: LayoutModifyBinding = LayoutModifyBinding.inflate(layoutInflater)
        activity?.let {
            AlertDialog.Builder(it)
                .setView(modify.getRoot())
                .setPositiveButton(
                    "确认修改", { d, i ->
                        val name: String = modify.editName.getText().toString().trim()
                        val oldPassword: String = modify.editOldPassword.getText().toString()
                        val password: String = modify.editPassword.getText().toString()
                        val passwordSure: String = modify.editPasswordSure.getText().toString()
                        if (name.isEmpty() || oldPassword.isEmpty()) {
                            Toast.makeText(it, "账号密码输入错误", Toast.LENGTH_SHORT).show()
                        } else if (isRegister(name, oldPassword)) {
                            if (password.isEmpty() || passwordSure.isEmpty()) {
                                Toast.makeText(it, "新密码不能为空", Toast.LENGTH_SHORT).show()
                            } else if (password == passwordSure) {
                                val db = database.getRead()
                                db.execSQL(
                                    "update $DBHelper.TABLE_USER set $DBHelper.PASSWORD = ? where $DBHelper.NAME = ?",
                                    arrayOf(password, name)
                                )
                                db.close()
                                isLogin = false
                                userName = "未登录"
                                setText()
                                Toast.makeText(it, "修改成功，请重新登录", Toast.LENGTH_SHORT).show()
                                startActivity(Intent(it, LoginActivity::class.java))
                            } else {
                                Toast.makeText(it, "确认密码不一致", Toast.LENGTH_SHORT).show()
                            }
                        } else {
                            Toast.makeText(it, "账号密码错误", Toast.LENGTH_SHORT).show()
                        }
                    }).show()
        }
    }


    /**
     * 判断用户输入密码是否正确
     */
    private fun isRegister(name: String, password: String): Boolean {
        return try {
            val db = database.getRead()
            //查询是否有相同的用户名和密码
            val cursor = db.query(
                DBHelper.TABLE_USER, arrayOf(DBHelper.NAME, DBHelper.PASSWORD),
                "$DBHelper.NAME='$name' AND $DBHelper.PASSWORD='$password'",
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

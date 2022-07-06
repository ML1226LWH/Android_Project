package swu.lwh.news.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.viewpager.widget.ViewPager
import swu.lwh.news.R
import swu.lwh.news.databinding.ActivityMainBinding
import swu.lwh.news.fragment.HomeFragment
import swu.lwh.news.fragment.PersonFragment
import swu.lwh.news.fragment.VideoFragment


class MainActivity : AppCompatActivity() {
    val fragmentList = arrayListOf(HomeFragment(), VideoFragment(), PersonFragment())
    val bind: ActivityMainBinding by lazy { ActivityMainBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(bind.root)
        //设置fragment页面的缓存数量，3个页面所以设置3
        bind.contentViewpager.offscreenPageLimit = 2
        bind.contentViewpager.adapter = MyAdapter(supportFragmentManager)
        bind.bottomNav.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.nav_home -> bind.contentViewpager.currentItem = 0
                R.id.nav_test_1 -> bind.contentViewpager.currentItem = 1
                R.id.nav_test_2 -> bind.contentViewpager.currentItem = 2
            }
            false
        }
        //给viewPager创建页面切换事件
        bind.contentViewpager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {
            }

            override fun onPageSelected(position: Int) {
                bind.bottomNav.menu.getItem(position).isChecked = true
            }

            override fun onPageScrollStateChanged(state: Int) {}
        })
    }

    inner class MyAdapter(fm: FragmentManager) :
        FragmentPagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
        override fun getCount(): Int {
            return fragmentList.size
        }

        override fun getItem(position: Int): Fragment {
            return fragmentList[position]
        }
    }
}
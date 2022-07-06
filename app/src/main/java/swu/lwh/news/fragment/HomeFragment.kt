package swu.lwh.news.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import swu.lwh.news.Util.showToast
import swu.lwh.news.activity.SearchActivity
import swu.lwh.news.adapter.PagerAdapter
import swu.lwh.news.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {
    val newsTypeList = listOf("top", "guonei", "guoji", "yule", "tiyu", "junshi")
    val titleList = arrayListOf("推荐", "国内", "国际", "娱乐", "体育", "军事")
    val fragmentList = ArrayList<Fragment>()

    val bind: FragmentHomeBinding by lazy { FragmentHomeBinding.inflate(layoutInflater) }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        bind.homeEditText.keyListener = null
        for (newsType in newsTypeList) {
            fragmentList.add(NewsFragment.newInstance(newsType))
        }
        bind.homeEditText.setOnClickListener {
            startActivity(Intent(activity, SearchActivity::class.java))
        }
        bind.avatar.setOnClickListener {
            "你点击了头像".showToast()
        }
        bind.newsViewPager.adapter = PagerAdapter(childFragmentManager, fragmentList, titleList)
        bind.newsTabLayout.setupWithViewPager(bind.newsViewPager)

        return bind.root
    }
}
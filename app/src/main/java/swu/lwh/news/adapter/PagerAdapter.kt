package swu.lwh.news.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter


open class PagerAdapter(
    fm: FragmentManager,
    val fragments: MutableList<Fragment>,
    val tabList: ArrayList<String>? = null
) : FragmentStatePagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    override fun getCount(): Int {
        return fragments.size
    }

    override fun getItem(position: Int): Fragment {
        return fragments.get(position)
    }

    //ViewPager与TabLayout绑定后，这里获取到PageTitle就是Tab的Text
    override fun getPageTitle(position: Int): CharSequence? {
        return tabList?.get(position)
    }
}
package swu.lwh.news.fragment

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.gson.Gson
import kotlinx.android.synthetic.main.fragment_video.*
import okhttp3.OkHttpClient
import okhttp3.Request
import swu.lwh.news.Util.RecyclerUtils
import swu.lwh.news.adapter.VideoAdapter
import swu.lwh.news.databinding.FragmentVideoBinding
import swu.lwh.news.model.VideoResponse
import kotlin.concurrent.thread

class VideoFragment : Fragment() {
    private val adapter = VideoAdapter()
    private val bind: FragmentVideoBinding by lazy { FragmentVideoBinding.inflate(layoutInflater) }

    private fun refresh_video() {
        thread {
            val request = Request.Builder()
                .url("https://api.apiopen.top/api/getHaoKanVideo?page=0")
                .build()
            val response = OkHttpClient().newCall(request).execute()//发送请求，返回的数据保存在response里
            val json = response.body?.string()
            val videoResponse = Gson().fromJson(json, VideoResponse::class.java)

            videoResponse?.result?.apply { activity?.runOnUiThread { adapter.refresh(list) } }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        RecyclerUtils.initList(activity, bind.recycler)
        bind.recycler.adapter = adapter
        refresh_video()
        bind.videoFresh.setColorSchemeColors(Color.RED)
        bind.videoFresh.setOnRefreshListener {
            thread {
                Thread.sleep(700)
                activity?.runOnUiThread {
                    refresh_video()
                    bind.videoFresh.isRefreshing = false
                }
            }
        }
        return bind.root
    }
}

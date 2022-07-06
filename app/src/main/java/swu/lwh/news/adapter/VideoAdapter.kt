package swu.lwh.news.adapter

import android.net.Uri
import swu.lwh.news.R
import swu.lwh.news.databinding.ItemVideoBinding
import swu.lwh.news.model.Video

class VideoAdapter : BaseAdapter<Video, ItemVideoBinding>(R.layout.item_video) {

    override fun bindData(binding: ItemVideoBinding, position: Int, item: Video) {
        binding?.apply {
            model = item
            videoContent.setVideoURI(Uri.parse(item.playUrl))
            playButton.setOnClickListener {
                item.isPlayer = !item.isPlayer
                if (item.isPlayer) {
                    playButton.setImageResource(R.drawable.ic_pause_blue)
                    videoContent.start()
                } else {
                    playButton.setImageResource(R.drawable.ic_start_blue)
                    videoContent.pause()
                }
            }
        }
    }
}
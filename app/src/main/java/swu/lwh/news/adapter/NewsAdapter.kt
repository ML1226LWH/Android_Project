package swu.lwh.news.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import swu.lwh.news.MyApplication
import swu.lwh.news.R
import swu.lwh.news.activity.DetailActivity
import swu.lwh.news.model.News

class NewsAdapter : RecyclerView.Adapter<NewsAdapter.BaseViewHolder>() {

    private val newsList = ArrayList<News>()
    fun setList(list: List<News>) {
        newsList.clear()
        newsList.addAll(list)
        notifyDataSetChanged()
    }

    fun remove(news: News) {
        newsList.remove(news)
        notifyDataSetChanged()
    }

    var collection: (News) -> Unit = {}

    companion object {
        // 三种不同的列表项:两种新闻列表项 + 底部列表项footer_view
        const val ONE_IMAGE_VIEW_TYPE = 1
        const val THREE_IMAGES_VIEW_TYPE = 3
        const val NONE_IMAGES_VIEW_TYPE = 0
        const val FOOTER_VIEW_TYPE = -1

        // 定义 footer_view的几种可能状态
        /**
         * HAS_MORE状态：footer_view 的进度条转圈,可执行 loadCacheData()
         */
        const val HAS_MORE = 996

        /**
         * FINISHED状态：footer_view 显示"已经没有更多内容了",不可执行 loadCacheData()
         */
        const val FINISHED = 997

        /**
         * FAILED状态：footer_view 显示"加载失败,点击重新加载",不可执行 loadCacheData()
         */
        const val FAILED = 998
    }

    /**
     * footer_view的当前状态
     */
    var footerViewStatus: Int = HAS_MORE

    // 列表项中增加了一个 footer_view,因此要+1
    override fun getItemCount(): Int = newsList.size + 1

    // 判断第position条新闻应该用哪一种列表项展示，返回viewType
    override fun getItemViewType(position: Int): Int {
        if (position == itemCount - 1) {
            // 最后一个列表项放 footer_view
            return FOOTER_VIEW_TYPE
        } else {
            val news = newsList[position]
            // news.thumbnail_pic_s02 == "null"必不可少 因为有些离谱的库会把 null序列化为"null"
            if ((news.thumbnail_pic_s03 == null
                        || news.thumbnail_pic_s03 == ""
                        || news.thumbnail_pic_s03 == "null")
                && (news.thumbnail_pic_s02 == null
                        || news.thumbnail_pic_s02 == ""
                        || news.thumbnail_pic_s02 == "null")
                && (news.thumbnail_pic_s == null
                        || news.thumbnail_pic_s == ""
                        || news.thumbnail_pic_s == "null")
            ) {
                return NONE_IMAGES_VIEW_TYPE
            } else if (news.thumbnail_pic_s02 == null
                || news.thumbnail_pic_s02 == ""
                || news.thumbnail_pic_s02 == "null"
                || news.thumbnail_pic_s03 == null
                || news.thumbnail_pic_s03 == ""
                || news.thumbnail_pic_s03 == "null"
            ) {
                return ONE_IMAGE_VIEW_TYPE
            } else {
                return THREE_IMAGES_VIEW_TYPE
            }
        }
    }

    // 根据不同的viewType加载不同的列表项布局
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        when (viewType) {
            THREE_IMAGES_VIEW_TYPE -> {
                val itemView =
                    LayoutInflater.from(parent.context)
                        .inflate(R.layout.news_item_three_images, parent, false)
                return ThreeImagesViewHolder(itemView)
            }
            ONE_IMAGE_VIEW_TYPE -> {
                val itemView =
                    LayoutInflater.from(parent.context)
                        .inflate(R.layout.news_item_one_image, parent, false)
                return OneImageViewHolder(itemView)
            }
            NONE_IMAGES_VIEW_TYPE -> {
                val itemView =
                    LayoutInflater.from(parent.context)
                        .inflate(R.layout.news_item_no_image, parent, false)
                return NoneImageViewHolder(itemView)
            }
            else -> {
                // 其它情况只会是 FOOTER_VIEW_TYPE
                val itemView =
                    LayoutInflater.from(parent.context)
                        .inflate(R.layout.footer_view, parent, false)
                return FooterViewHolder(itemView)
            }
        }
    }

    // 将数据展示在列表项中,即刷新列表项的UI
    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        if (holder is NewsViewHolder) {
            val news = newsList[position]
            // (1)显示新闻标题
            holder.title.text = news.title
            // (2)显示新闻描述:用 author_name和 date两个字段拼接出来
            val stringBuilder = StringBuilder()
            stringBuilder.append(news.author_name)
                .append("      ").append(news.date)
            holder.description.text = stringBuilder.toString()
            // (3)加载新闻图片
            when (holder) {
                is OneImageViewHolder -> {
                    Glide.with(MyApplication.context).load(news.thumbnail_pic_s)
                        .into(holder.image)
                }
                is ThreeImagesViewHolder -> {
                    Glide.with(MyApplication.context).load(news.thumbnail_pic_s)
                        .into(holder.image1)
                    Glide.with(MyApplication.context).load(news.thumbnail_pic_s02)
                        .into(holder.image2)
                    Glide.with(MyApplication.context).load(news.thumbnail_pic_s03)
                        .into(holder.image3)
                }
            }
            // (4)列表项点击事件
            holder.itemView.setOnClickListener {
                // holder.adapterPosition 到底是什么? 当前点击的新闻在newsList中的下标
                val intent = Intent(MyApplication.context, DetailActivity::class.java)
                val currentNews = newsList[holder.adapterPosition]
                intent.putExtra("news_from=", currentNews.author_name)
                intent.putExtra("url=", currentNews.url)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                startActivity(MyApplication.context, intent, null)
            }
            holder.itemView.setOnLongClickListener {
                collection(news)
                return@setOnLongClickListener true
            }
        } else {
            // 其它情况只会是 FooterViewHolder
            val footerViewHolder = holder as FooterViewHolder
            when (footerViewStatus) {
                HAS_MORE -> {
                    footerViewHolder.processBar.visibility = View.VISIBLE
                    footerViewHolder.message.text = "正在加载中..."
                }
                FAILED -> {
                    footerViewHolder.processBar.visibility = View.GONE
                    footerViewHolder.message.text = "加载失败,点击重新加载"
                }
                FINISHED -> {
                    footerViewHolder.processBar.visibility = View.GONE
                    footerViewHolder.message.text = "已经没有更多内容了"
                }
            }
            footerViewHolder.itemView.setOnClickListener {
                footerViewHolder.processBar.visibility = View.VISIBLE
                footerViewHolder.message.text = "正在加载中..."
                // 将状态调整成HAS_MORE执行一次loadCacheData()
                footerViewStatus = HAS_MORE
            }
        }
    }

    // 列表项基类
    open inner class BaseViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    // 新闻列表项基类(无论是哪种类型的新闻列表项，都有标题和描述)
    open inner class NewsViewHolder(itemView: View) : BaseViewHolder(itemView) {
        val title: TextView = itemView.findViewById(R.id.news_title)
        val description: TextView = itemView.findViewById(R.id.news_desc)
    }

    // footer_view也是一个列表项
    open inner class FooterViewHolder(itemView: View) : BaseViewHolder(itemView) {
        val processBar: ProgressBar = itemView.findViewById(R.id.progress_bar)
        val message: TextView = itemView.findViewById(R.id.message)
    }

    // 只有一张图片的新闻列表项
    inner class NoneImageViewHolder(itemView: View) : NewsViewHolder(itemView) {
    }

    // 只有一张图片的新闻列表项
    inner class OneImageViewHolder(itemView: View) : NewsViewHolder(itemView) {
        val image: com.makeramen.roundedimageview.RoundedImageView =
            itemView.findViewById(R.id.news_image)
    }

    // 有三张图片的新闻列表项
    inner class ThreeImagesViewHolder(itemView: View) : NewsViewHolder(itemView) {
        val image1: com.makeramen.roundedimageview.RoundedImageView =
            itemView.findViewById(R.id.news_image_1)
        val image2: com.makeramen.roundedimageview.RoundedImageView =
            itemView.findViewById(R.id.news_image_2)
        val image3: com.makeramen.roundedimageview.RoundedImageView =
            itemView.findViewById(R.id.news_image_3)
    }
}
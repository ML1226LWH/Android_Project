package swu.lwh.news.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.IntRange
import androidx.annotation.NonNull
import androidx.annotation.Nullable
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import java.util.*


/**
 * BaseAdapter
 * Created by liufeng on 2021/12/1
 */
abstract class BaseAdapter<T, DB : ViewDataBinding>(val layoutId: Int) :
    RecyclerView.Adapter<BaseAdapter.BaseViewHolder>() {
    protected var mDatas = arrayListOf<T>() //数据集合
    protected var mContext: Context? = null
    protected var onItemClickListener: ((BaseAdapter<T, DB>, Int) -> Unit)? = null
    protected var onItemLongClickListener: ((BaseAdapter<T, DB>, Int) -> Boolean)? = null

    /**
     * 添加单条数据
     *
     * @param t
     */
    fun addOneData(t: T) {
        mDatas.clear()
        mDatas.add(t)
        notifyDataSetChanged()
    }

    /**
     * 删除单条数据
     *
     * @param t
     */
    fun remove(t: T) {
        mDatas.remove(t)
        notifyDataSetChanged()
    }

    /**
     * 刷新数据
     *
     * @param datas
     */
    open fun refresh(datas: List<T>?) {
        mDatas.clear()
        datas?.let { mDatas.addAll(it) }
        notifyDataSetChanged()
    }

    /**
     * 添加数据
     *
     * @param datas
     */
    open fun addData(datas: List<T>) {
        mDatas.addAll(datas)
        notifyDataSetChanged()
    }

    /**
     * 绑定数据
     *
     * @param binding  具体的binding
     * @param position 对应的索引
     * @param t
     */
    protected abstract fun bindData(binding: DB, position: Int, item: T)

    fun setOnItemClick(onItemClickListener: (BaseAdapter<T, DB>, Int) -> Unit) {
        this.onItemClickListener = onItemClickListener
    }

    fun setOnItemLongClick(onItemLongClickListener: (BaseAdapter<T, DB>, Int) -> Boolean) {
        this.onItemLongClickListener = onItemLongClickListener
    }

    @NonNull
    open fun getData(): ArrayList<T> {
        return mDatas
    }

    @Nullable
    open fun getItem(@IntRange(from = 0L) position: Int): T? {
        return if (position >= 0 && position < mDatas.size) mDatas[position] else null
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        mContext = parent.context
        val binding: DB =
            DataBindingUtil.inflate(LayoutInflater.from(mContext), layoutId, null, false)
        val holder = BaseViewHolder(binding)
        holder.binding?.apply {
            onItemClickListener?.let {
                root.setOnClickListener { v -> it(this@BaseAdapter, holder.layoutPosition) }
            }
            onItemLongClickListener?.let {
                root.setOnLongClickListener { v -> it(this@BaseAdapter, holder.layoutPosition) }
            }
            val params = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
            root.setLayoutParams(params)
        }
        return holder
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        try {
            val t: T? = if (mDatas.size > 0) mDatas[position] else null
            t?.let { bindData(holder.binding as DB, position, it) }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun getItemCount(): Int {
        return mDatas.size
    }

    /**
     * 封装ViewHolder ,子类可以直接使用
     */
    class BaseViewHolder(val binding: ViewDataBinding) : RecyclerView.ViewHolder(binding.root)
}
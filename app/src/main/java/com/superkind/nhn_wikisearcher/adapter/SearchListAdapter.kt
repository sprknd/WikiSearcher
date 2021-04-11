package com.superkind.nhn_wikisearcher.adapter

import android.app.Activity
import android.content.Context
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import com.superkind.nhn_wikisearcher.databinding.ListItemBinding
import com.superkind.nhn_wikisearcher.view.WikiItemView
import com.superkind.nhn_wikisearcher.vo.WikiSearchResult
import java.lang.Exception

class SearchListAdapter (val context: Context) : BaseAdapter() {
    private var items: ArrayList<WikiSearchResult> = arrayListOf()

    override fun getCount(): Int {
        return items.size
    }

    override fun getItem(position: Int): Any {
        return items[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val holder: WikiItemHolder
        lateinit var itemView: WikiItemView

        // contentView가 null일때(최초로 화면이 실행될 때)
        // ViewHolder에 바인딩된 View들을 연결시킨 후 tag에 Holder를 저장한다
        if (convertView == null) {
            itemView = WikiItemView(context)
            itemView.setOnClickListener()

            holder = WikiItemHolder(itemView.getImgView(), itemView.getTitleView(), itemView.getExtractView())
            itemView.tag = holder
        } else { // 뷰가 recycle 됐을 때, tag에 저장되어 있던 홀더를 꺼낸다.
            holder = convertView.tag as WikiItemHolder
            itemView = convertView as WikiItemView
        }

        // Holder에 저장되어 있던 데이터를 변경한다.
        with (items[position]) {
            holder.image.setImageDrawable(img)
            holder.title.text = displayTitle
            holder.extract.text = extract
        }

        return itemView
    }

    /**
     * 리스트 배열을 교체합니다.
     */
    fun setDataList(list: ArrayList<WikiSearchResult>) {
        items = list
        notifyDataSetChanged()
    }

    /**
     * 리스트 아이템을 추가합니다.
     */
    fun addData(item: WikiSearchResult) {
        try {
            items.add(item)
            notifyDataSetChanged()
        } catch (e: Exception) {
            Log.d("superkind-debug", e.message.toString())
        }
    }

    /**
     * 리스트 아이템을 제거합니다.
     */
    fun removeItem(item: WikiSearchResult, position: Int) {
        try {
            items.removeAt(position)
            notifyDataSetChanged()
        } catch (e: Exception) {
            Log.d("superkind-debug", e.message.toString())
        }
    }

    /**
     * 리스트를 초기화합니다.
     */
    fun clearList() {
        items = arrayListOf()
    }

    /**
     * 각 아이템 데이터를 담을 Holder
     */
    data class WikiItemHolder(
        var image: ImageView,
        var title: TextView,
        var extract: TextView
    )
}


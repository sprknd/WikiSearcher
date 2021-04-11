package com.superkind.nhn_wikisearcher.view

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import com.superkind.nhn_wikisearcher.activity.SearchActivity
import com.superkind.nhn_wikisearcher.databinding.ListHeaderBinding
import com.superkind.nhn_wikisearcher.databinding.ListItemBinding

class WikiItemView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0)
    : ConstraintLayout(context, attrs, defStyleAttr) {

    private val binding = ListItemBinding.inflate(LayoutInflater.from(context), this, true)

    fun setImg(drawable: Drawable) {
        binding.imgThumbnail.setImageDrawable(drawable)
    }

    fun setImgSize(width: Int, height: Int) {
        binding.imgThumbnail.layoutParams.width = width
        binding.imgThumbnail.layoutParams.height = height
        binding.imgThumbnail.requestLayout()
    }

    fun setTitle(title: String) {
        binding.txDisplayTitle.text = title
    }

    fun setExtract(extract: String) {
        binding.txExtract.text = extract
    }

    fun getImgView(): ImageView {
        return binding.imgThumbnail
    }

    fun getTitleView(): TextView {
        return binding.txDisplayTitle
    }

    fun getExtractView(): TextView {
        return binding.txExtract
    }

    fun setOnClickListener() {
        binding.listItem.setOnClickListener {
            val intent = Intent(context.applicationContext, SearchActivity::class.java)
            intent.putExtra("search", binding.txDisplayTitle.text)
            context.startActivity(intent)
        }
    }
}
package com.superkind.nhnsoft_wikiviewer.view

import android.content.Context
import android.content.Intent
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.ImageView
import androidx.constraintlayout.widget.ConstraintLayout
import com.superkind.nhnsoft_wikiviewer.activity.WebViewActivity
import com.superkind.nhnsoft_wikiviewer.databinding.ListHeaderBinding

open class WikiHeaderView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0)
    : ConstraintLayout(context, attrs, defStyleAttr) {

    private val binding = ListHeaderBinding.inflate(LayoutInflater.from(context), this, true)

    fun setImg(drawable: Drawable) {
        binding.imgHeader.setImageDrawable(drawable)
        binding.imgHeader.scaleType = ImageView.ScaleType.FIT_CENTER
    }

    fun setImgSize(width: Int, height: Int) {
        binding.imgHeader.layoutParams.width = width
        binding.imgHeader.layoutParams.height = height
        binding.imgHeader.requestLayout()
    }

    fun setTitle(title: String) {
        binding.txDisplayTitle.text = title
    }

    fun setExtract(extract: String) {
        binding.txExtract.text = extract
    }

    fun setOnClickListener() {
        binding.listHeader.setOnClickListener {
            val intent = Intent(context.applicationContext, WebViewActivity::class.java)
            intent.putExtra("search", binding.txDisplayTitle.text)
            context.startActivity(intent)
        }
    }
}
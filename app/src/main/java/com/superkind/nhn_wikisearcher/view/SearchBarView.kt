package com.superkind.nhn_wikisearcher.view

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import com.superkind.nhn_wikisearcher.databinding.BarSearchBinding

class SearchBarView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0)
    : ConstraintLayout(context, attrs, defStyleAttr) {

        private val mBinding = BarSearchBinding.inflate(LayoutInflater.from(context), this, true)

    fun setBackOnClickListener(listener: OnClickListener) {
        mBinding.imgBtnBack.setOnClickListener(listener)
    }

    fun setSearchOnClickListener(listener: OnClickListener) {
        mBinding.imgBtnSearch.setOnClickListener(listener)
    }

    fun setEditOnKeyListener(listener: View.OnKeyListener) {
        mBinding.txSearch.setOnKeyListener(listener)
    }

    fun getSearchText(): String {
        return mBinding.txSearch.text.toString()
    }

    fun setSearchText(text: String) {
        mBinding.txSearch.setText(text)
    }

    fun setSearchTextEnabled(enable: Boolean) {
        mBinding.txSearch.isEnabled = enable
    }
}
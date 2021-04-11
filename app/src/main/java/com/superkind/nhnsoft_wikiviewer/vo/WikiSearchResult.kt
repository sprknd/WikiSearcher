package com.superkind.nhnsoft_wikiviewer.vo

import android.graphics.drawable.Drawable

data class WikiSearchResult(
        var displayTitle: String,   // 타이틀
        var extract: String,        // 요약정보
        var imgSrc: String,         // 이미지 주소
        var imgDrawable: Drawable?, // 이미지 Drawable
        var imgWidth: Int,          // 이미지 Width
        var imgHeight: Int          // 이미지 Height
)

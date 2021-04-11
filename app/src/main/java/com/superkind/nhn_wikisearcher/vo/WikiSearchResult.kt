package com.superkind.nhn_wikisearcher.vo

import android.graphics.drawable.Drawable

data class WikiSearchResult(
    var displayTitle: String,
    var extract: String,
    var imgSrc: String,
    var img: Drawable?,
    var imgWidth: Int,
    var imgHeight: Int,
)

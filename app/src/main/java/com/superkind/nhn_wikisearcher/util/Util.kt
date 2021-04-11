package com.superkind.nhn_wikisearcher.util

object Util {
    fun makeSearchText(search: String) : String {
        return search.replace(" ", "_")
                .replace("<i>", "")
                .replace("</i>", "")
    }
}
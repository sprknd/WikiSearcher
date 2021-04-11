package com.superkind.nhn_wikisearcher.util

object Util {
    fun makeDisplayText(search: String): String {
        return search.replace("<i>", "")
                .replace("</i>", "")
    }
    fun makeSearchText(search: String): String {
        return search.replace(" ", "_")
                .replace("<i>", "")
                .replace("</i>", "")
    }
}
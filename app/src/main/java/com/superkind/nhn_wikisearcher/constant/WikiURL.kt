package com.superkind.nhn_wikisearcher.constant

object WikiURL {
    private const val baseUri: String = "https://en.wikipedia.org/api/rest_v1/page/"
    const val detail: String = baseUri + "html/"
    const val summary: String = baseUri + "summary/"
    const val related: String = baseUri + "related/"
}

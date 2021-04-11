package com.superkind.httpapi.interfaces

interface HttpCallBack {
    fun onSuccess(response: ByteArray)
    fun onFailure(code: Int, msg: String)
}
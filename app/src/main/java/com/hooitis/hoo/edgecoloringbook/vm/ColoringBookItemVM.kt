package com.hooitis.hoo.edgecoloringbook.vm

import android.arch.lifecycle.MutableLiveData
import android.graphics.Bitmap
import com.hooitis.hoo.edgecoloringbook.base.BaseViewModel


@Suppress("unused")
class ColoringBookItemVM: BaseViewModel() {
    val coloringBook: MutableLiveData<String> = MutableLiveData()

    fun bind(url: String){
        this.coloringBook.value = url
    }
}
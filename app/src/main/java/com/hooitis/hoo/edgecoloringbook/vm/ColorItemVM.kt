package com.hooitis.hoo.edgecoloringbook.vm

import android.arch.lifecycle.MutableLiveData
import com.hooitis.hoo.edgecoloringbook.base.BaseViewModel
import com.hooitis.hoo.edgecoloringbook.model.quiz.Quiz


@Suppress("unused")
class ColorItemVM: BaseViewModel() {
    val color: MutableLiveData<Int> = MutableLiveData()

    fun bind(color: Int){
        this.color.value = color
    }
}
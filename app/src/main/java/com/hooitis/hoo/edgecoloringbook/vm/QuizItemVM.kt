package com.hooitis.hoo.edgecoloringbook.vm

import android.arch.lifecycle.MutableLiveData
import com.hooitis.hoo.edgecoloringbook.base.BaseViewModel
import com.hooitis.hoo.edgecoloringbook.model.quiz.Quiz


@Suppress("unused")
class QuizItemVM: BaseViewModel() {
    val imageUrl: MutableLiveData<String> = MutableLiveData()
    val origin: MutableLiveData<String> = MutableLiveData()
    val meaning: MutableLiveData<String> = MutableLiveData()
    val qProverb: MutableLiveData<String> = MutableLiveData()
    val aProverb: MutableLiveData<String> = MutableLiveData()

    fun bind(quiz: Quiz){
        this.meaning.value = quiz.meaning
        this.qProverb.value = quiz.questionProverb
        this.aProverb.value = quiz.answerProverb
    }
}
package com.hooitis.hoo.edgecoloringbook.vm

import android.arch.lifecycle.MutableLiveData
import android.util.Log
import com.hooitis.hoo.edgecoloringbook.base.BaseViewModel
import com.hooitis.hoo.edgecoloringbook.model.quiz.Quiz
import com.hooitis.hoo.edgecoloringbook.model.quiz.QuizRepository
import com.hooitis.hoo.edgecoloringbook.ui.ColorListAdapter
import com.hooitis.hoo.edgecoloringbook.ui.QuizImageListAdapter
import com.hooitis.hoo.edgecoloringbook.utils.COUNTDOWN
import com.hooitis.hoo.edgecoloringbook.utils.DRAWING_MODE
import java.util.*
import javax.inject.Inject
import kotlin.concurrent.schedule


@Suppress("unused")
class MainVM @Inject constructor(
        private val quizRepository: QuizRepository
): BaseViewModel() {

    private var index = 0

    val scaleFactor: MutableLiveData<Float> = MutableLiveData()
    val brushType: MutableLiveData<Int> = MutableLiveData()
    val brushColor: MutableLiveData<Int> = MutableLiveData()
    val drawingMode: MutableLiveData<Int> = MutableLiveData()
    val processingImage: MutableLiveData<Int> = MutableLiveData()

    val colorListAdapter: ColorListAdapter by lazy {
        ColorListAdapter(this)
    }

    init {
        index = 0
        scaleFactor.value = 1f
        brushType.value = 0
        drawingMode.value = DRAWING_MODE
    }

}
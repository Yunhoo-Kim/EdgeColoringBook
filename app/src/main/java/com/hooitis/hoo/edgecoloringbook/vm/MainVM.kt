package com.hooitis.hoo.edgecoloringbook.vm

import android.arch.lifecycle.MutableLiveData
import android.util.Log
import com.hooitis.hoo.edgecoloringbook.base.BaseViewModel
import com.hooitis.hoo.edgecoloringbook.model.quiz.Quiz
import com.hooitis.hoo.edgecoloringbook.model.quiz.QuizRepository
import com.hooitis.hoo.edgecoloringbook.ui.QuizImageListAdapter
import com.hooitis.hoo.edgecoloringbook.utils.COUNTDOWN
import java.util.*
import javax.inject.Inject
import kotlin.concurrent.schedule


@Suppress("unused")
class MainVM @Inject constructor(
        private val quizRepository: QuizRepository
): BaseViewModel() {

    private var index = 0
    private lateinit var timer: Timer
    private lateinit var quizList: List<Quiz>


    val wrong: MutableLiveData<Boolean> = MutableLiveData()
    val quizIndex: MutableLiveData<Int> = MutableLiveData()
    val countDown: MutableLiveData<String> = MutableLiveData()

    val quizImageListAdapter: QuizImageListAdapter by lazy {
        QuizImageListAdapter()
    }

    init {
        wrong.value = false
        index = 0
        quizIndex.value = index
        countDown.value = COUNTDOWN
    }

    fun loadQuizData(){
        quizList = quizRepository.getQuizzes().blockingFirst().shuffled()
        quizImageListAdapter.updateQuizList(quizList)

//        for(i in quizList){
//            Log.d("LoadQuiz", i.imageUrl)
//
//            for(j in i.answerList){
//                Log.d("Quiz Answer", j)
//            }
//        }
        resetTimer()
    }

    private fun resetTimer() {
        countDown.postValue(COUNTDOWN)

        if(::timer.isInitialized) {
            timer.cancel()
            timer.purge()
        }

        timer = Timer("Count", false)
        timer.schedule(1000, 1000){
            val cnt = countDown.value!!.toInt() - 1
            if(cnt < 0){
                wrong.postValue(true)
                cancel()
            }else {
                countDown.postValue(cnt.toString())
            }
        }
    }

    private fun showNextQuiz(){
        if(wrong.value!!)
            return

        quizIndex.postValue(index + 1)
        index++
        resetTimer()
    }

    fun checkResult(results: List<String>): Boolean{
        var found = false

        val quiz = quizList[index]
//        quiz.answerList.forEachIndexed { _, value ->
//            Log.d("Quiz Answer", value)
//           if(results.contains(value)){
//               found = true
//               wrong.value = false
//               showNextQuiz()
//               return@forEachIndexed
//           }
//        }

//        if (!found){
//            timer.cancel()
//            timer.purge()
//            wrong.postValue(true)
//        }

        return found
    }

}
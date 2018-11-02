package com.hooitis.hoo.edgecoloringbook.model.quiz

import android.util.Log
//import com.google.firebase.firestore.FirebaseFirestore
import com.hooitis.hoo.edgecoloringbook.model.SharedPreferenceHelper
import com.hooitis.hoo.edgecoloringbook.utils.FIREBASE_DB_URL
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject

@Suppress("unused")
class QuizRepository @Inject constructor(private val quizDao: QuizDao,
//                                         private val firebaseStore: FirebaseFirestore,
                                         private val sharedPreferenceHelper: SharedPreferenceHelper){


    val mCompositeDisposable: CompositeDisposable by lazy {
        CompositeDisposable()
    }

    init {
    }

//    fun loadQuizFromStore(): Observable<List<Quiz>> = Observable.create { emitter ->
//        firebaseStore.collection(FIREBASE_DB_URL).get().addOnCompleteListener { res ->
//            if (res.isSuccessful) {
//                for (doc in res.result!!) {
//                        quizDao.insert(Quiz(
//                                id = doc.data["id"].toString().toLong(),
//                                questionProverb = doc.data["questionProverb"].toString(),
//                                answerProverb = doc.data["answerProverb"].toString(),
//                                proverb = doc.data["proverb"].toString(),
//                                meaning = doc.data["meaning"].toString()))
//                }
//                emitter.onNext(quizDao.get())
//            }else{
//                Log.d("LoadQuiz", "error")
//            }
//        }.addOnFailureListener {err ->
//            Log.d("error", err.toString())
//        }
//    }

    private fun loadQuiz(): Observable<List<Quiz>>{
        return Observable.fromCallable { quizDao.get() }
                .concatMap {
                    if(it.isEmpty())
//                        loadQuizFromStore()
                    Observable.just(it)

                    else {
                        Observable.just(it)
                    }
                }
    }

    fun saveQuizzes(quizzes : List<Quiz>): Observable<List<Quiz>> {
        for(q in quizzes)
            quizDao.insert(q)
        return Observable.just(quizzes)
    }

    fun saveQuiz(quiz: Quiz)  = quizDao.insert(quiz)

    private fun initQuizzes(){
//        mCompositeDisposable.dispose()
//        mCompositeDisposable.add(loadFoods()
//                .subscribe({
//                }, {
//                }, {
//                }))
    }

    fun getQuizzes(): Observable<List<Quiz>> = Observable.just(quizDao.get())
}

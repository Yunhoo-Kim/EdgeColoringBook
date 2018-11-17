package com.hooitis.hoo.edgecoloringbook.model.coloringbook

import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import com.hooitis.hoo.edgecoloringbook.model.SharedPreferenceHelper
import com.hooitis.hoo.edgecoloringbook.utils.FIREBASE_DB_URL
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject

@Suppress("unused")
class ColoringBookRepository @Inject constructor(private val coloringBookDao: ColoringBookDao,
                                                 private val firebaseStore: FirebaseFirestore,
                                                 private val sharedPreferenceHelper: SharedPreferenceHelper){


    val mCompositeDisposable: CompositeDisposable by lazy {
        CompositeDisposable()
    }

    init {
    }

    fun loadColoringBookFromStore(): Observable<List<ColoringBook>> = Observable.create { emitter ->
        firebaseStore.collection(FIREBASE_DB_URL).get().addOnCompleteListener { res ->
            if (res.isSuccessful) {
                for (doc in res.result!!) {
                    coloringBookDao.insert(ColoringBook(
                            id = doc.data["id"].toString().toLong(),
                            url = doc.data["url"].toString()))
                }
                emitter.onNext(coloringBookDao.get())
            }else{
            }
        }.addOnFailureListener {err ->
            Log.d("error", err.toString())
        }
    }

    private fun loadColoringBook(): Observable<List<ColoringBook>>{
        return Observable.fromCallable { coloringBookDao.get() }
                .concatMap {
                    if(it.isEmpty())
//                        loadQuizFromStore()
                        Observable.just(it)

                    else {
                        Observable.just(it)
                    }
                }
    }

    fun saveColoringBook(coloringBooks: List<ColoringBook>): Observable<List<ColoringBook>> {
        for(c in coloringBooks)
            coloringBookDao.insert(c)

        return Observable.just(coloringBooks)
    }

    fun saveQuiz(coloringBook: ColoringBook)  = coloringBookDao.insert(coloringBook)

    private fun initQuizzes(){
//        mCompositeDisposable.dispose()
//        mCompositeDisposable.add(loadFoods()
//                .subscribe({
//                }, {
//                }, {
//                }))
    }

    fun getColoringBooks(): Observable<List<ColoringBook>> = Observable.just(coloringBookDao.get())
}

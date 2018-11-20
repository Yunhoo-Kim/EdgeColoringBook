package com.hooitis.hoo.edgecoloringbook.model.edgecoloringbook
import com.google.firebase.firestore.FirebaseFirestore
import com.hooitis.hoo.edgecoloringbook.model.SharedPreferenceHelper
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject

@Suppress("unused")
class TempColoringBookRepository @Inject constructor(val tempColoringBookDao: TempColoringBookDao,
                                                     val firebaseStore: FirebaseFirestore,
                                                     val sharedPreferenceHelper: SharedPreferenceHelper){


    val mCompositeDisposable: CompositeDisposable by lazy {
        CompositeDisposable()
    }
    init {
    }


    fun getTempColoringBooks(): List<Long> = tempColoringBookDao.get()
    fun getTempColoringBooks(id: Long): TempColoringBook  = tempColoringBookDao.get(id)
    fun deleteTempColoringBook(id: Long) = tempColoringBookDao.delete(id)

    fun saveTempColoringBook(temp: TempColoringBook): Observable<TempColoringBook> {
        tempColoringBookDao.insert(temp)
        return Observable.just(temp)
    }

}

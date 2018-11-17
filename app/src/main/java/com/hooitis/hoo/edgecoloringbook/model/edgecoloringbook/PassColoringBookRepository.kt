package com.hooitis.hoo.edgecoloringbook.model.edgecoloringbook
import com.google.firebase.firestore.FirebaseFirestore
import com.hooitis.hoo.edgecoloringbook.model.SharedPreferenceHelper
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject

@Suppress("unused")
class PassColoringBookRepository @Inject constructor(val passColoringBookDao: PassColoringBookDao) {


    val mCompositeDisposable: CompositeDisposable by lazy {
        CompositeDisposable()
    }

    init {
    }


    fun getPassColoringBooks(): PassColoringBook = passColoringBookDao.get(0)

    fun savePassColoringBook(pass: PassColoringBook): PassColoringBook{
        passColoringBookDao.insert(pass)
        return pass
    }

}

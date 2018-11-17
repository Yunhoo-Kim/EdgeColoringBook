package com.hooitis.hoo.edgecoloringbook.model.edgecoloringbook

//import com.google.firebase.firestore.FirebaseFirestore
import android.annotation.SuppressLint
import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import com.hooitis.hoo.edgecoloringbook.BuildConfig
import com.hooitis.hoo.edgecoloringbook.model.SharedPreferenceHelper
import com.hooitis.hoo.edgecoloringbook.model.coloringbook.ColoringBookRepository
import com.hooitis.hoo.edgecoloringbook.utils.FIREBASE_VERSION_DB
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject

@Suppress("unused")
class VersionsRepository @Inject constructor(val versionsDao: VersionsDao,
                                             val coloringBookRepository: ColoringBookRepository,
                                             val firebaseStore: FirebaseFirestore,
                                             val sharedPreferenceHelper: SharedPreferenceHelper){


    val mCompositeDisposable: CompositeDisposable by lazy {
        CompositeDisposable()
    }
    init {
    }

    fun loadVersionFromServer(): Observable<Versions> {
        return Observable.create { emitter ->
            firebaseStore.collection(FIREBASE_VERSION_DB).document(FIREBASE_VERSION_DB).get().addOnCompleteListener { res ->
                if (res.isSuccessful) {
                    emitter.onNext(
                            Versions(appVersion = res.result!!.data!!["appVersion"].toString().toLong(),
                                    dbVersion = res.result!!.data!!["dbVersion"].toString().toLong()))
                }else{
                    emitter.onError(Exception())
                }
            }.addOnFailureListener {err ->
                Log.d("error", err.toString())
            }
        }
    }

    fun loadLocalVersions(): Versions {
        var versionList = versionsDao.get()
        if(versionList.isEmpty()){
            versionsDao.insert(Versions(0, appVersion=BuildConfig.VERSION_CODE.toLong(), dbVersion = -1))
            versionList = versionsDao.get()
        }

        return versionList.first()
    }

    fun saveVersion(versions: Versions): Observable<Versions> {
        versionsDao.insert(versions)
        return Observable.just(versions)
    }

    fun checkVersion() = loadVersionFromServer()
//        mCompositeDisposable.add(loadVersionFromServer()
//                .subscribe({serverVersion ->
//                    val localVersion = loadLocalVersions()
//                    if(serverVersion.dbVersion != localVersion.dbVersion){
//                        coloringBookRepository.loadColoringBookFromStore().subscribe {  }
//                    }
//                    if(serverVersion.appVersion != localVersion.appVersion){
//                        // update application from store
//                    }
//                    versionsDao.deleteAll()
//                    versionsDao.insert(serverVersion)
//                }, {
//                }))
//    }
}

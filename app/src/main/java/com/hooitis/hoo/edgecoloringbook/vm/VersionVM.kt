package com.hooitis.hoo.edgecoloringbook.vm

import android.arch.lifecycle.MutableLiveData
import com.hooitis.hoo.edgecoloringbook.base.BaseViewModel
import com.hooitis.hoo.edgecoloringbook.model.edgecoloringbook.VersionsRepository
import javax.inject.Inject


@Suppress("unused")
class VersionVM @Inject constructor(
        val versionsRepository: VersionsRepository
): BaseViewModel() {

    val beforeQuizText: MutableLiveData<String> = MutableLiveData()

    init {
//        checkVersion()
    }

    fun checkVersion() = versionsRepository.checkVersion()
}
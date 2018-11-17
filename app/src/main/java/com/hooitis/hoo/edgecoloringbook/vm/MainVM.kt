package com.hooitis.hoo.edgecoloringbook.vm

import android.arch.lifecycle.MutableLiveData
import android.graphics.Color
import android.util.Log
import com.hooitis.hoo.edgecoloringbook.base.BaseViewModel
import com.hooitis.hoo.edgecoloringbook.model.coloringbook.ColoringBookRepository
import com.hooitis.hoo.edgecoloringbook.model.edgecoloringbook.PassColoringBook
import com.hooitis.hoo.edgecoloringbook.model.edgecoloringbook.PassColoringBookRepository
import com.hooitis.hoo.edgecoloringbook.model.edgecoloringbook.TempColoringBook
import com.hooitis.hoo.edgecoloringbook.model.edgecoloringbook.TempColoringBookRepository
import com.hooitis.hoo.edgecoloringbook.ui.ColorListAdapter
import com.hooitis.hoo.edgecoloringbook.ui.ColoringBookListAdapter
import com.hooitis.hoo.edgecoloringbook.ui.TempColoringBookListAdapter
import com.hooitis.hoo.edgecoloringbook.utils.TOUCH_MODE
import javax.inject.Inject


@Suppress("unused")
class MainVM @Inject constructor(
        private val coloringBookRepository: ColoringBookRepository,
        private val passColoringBookRepository: PassColoringBookRepository,
        private val tempColoringBookRepository: TempColoringBookRepository
): BaseViewModel() {

    private var index = 0

    val scaleFactor: MutableLiveData<Float> = MutableLiveData()
    val brushType: MutableLiveData<Int> = MutableLiveData()
    val brushColor: MutableLiveData<Int> = MutableLiveData()
    val drawingMode: MutableLiveData<Int> = MutableLiveData()
    val processingImage: MutableLiveData<Int> = MutableLiveData()
    val isFabOpen: MutableLiveData<Boolean> = MutableLiveData()
    val isWidthFabOpen: MutableLiveData<Boolean> = MutableLiveData()
    val imageUrl: MutableLiveData<String> = MutableLiveData()
    val tempImageId: MutableLiveData<Long> = MutableLiveData()
    val saveButtonVisibility: MutableLiveData<Boolean> = MutableLiveData()

    val colorListAdapter: ColorListAdapter by lazy {
        ColorListAdapter(this)
    }
    val coloringBookListAdapter: ColoringBookListAdapter by lazy {
        ColoringBookListAdapter(this)
    }

    val tempColoringBookListAdapter: TempColoringBookListAdapter by lazy {
        TempColoringBookListAdapter(this)
    }

    init {
        index = 0
        scaleFactor.value = 1f
        brushType.value = 0
        brushColor.value = Color.BLACK
        drawingMode.value = TOUCH_MODE
        saveButtonVisibility.value = true
    }

    fun getColoringBookList() {
        val coloringBooks = coloringBookRepository.getColoringBooks().blockingFirst()
        coloringBookListAdapter.updateColoringBookList(coloringBooks)
    }

    fun getTempColoringBookList() {
        val coloringBooks = tempColoringBookRepository.getTempColoringBooks()
        Log.d("Image", "${coloringBooks.size}")
        tempColoringBookListAdapter.updateTempColoringBookList(coloringBooks)
    }

    fun savePassColoringBook(passC: PassColoringBook) = passColoringBookRepository.savePassColoringBook(passC)
    fun getPassColoringBook() = passColoringBookRepository.getPassColoringBooks()
    fun saveTempColoringBook(tempC: TempColoringBook) = tempColoringBookRepository.saveTempColoringBook(tempC)
    fun getTempColoringBook(id: Long) = tempColoringBookRepository.getTempColoringBooks(id)
    fun getTempColoringBooks() = tempColoringBookRepository.getTempColoringBooks()
}
package com.hooitis.hoo.edgecoloringbook.ui

import android.content.Intent
import android.databinding.DataBindingUtil
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.support.v4.content.ContextCompat.startActivity
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import com.hooitis.hoo.edgecoloringbook.R
import com.hooitis.hoo.edgecoloringbook.databinding.ItemColoringbookBinding
import com.hooitis.hoo.edgecoloringbook.model.coloringbook.ColoringBook
import com.hooitis.hoo.edgecoloringbook.vm.ColoringBookItemVM
import com.hooitis.hoo.edgecoloringbook.vm.MainVM
import java.io.ByteArrayOutputStream


class ColoringBookListAdapter constructor(private val mainVM:MainVM) : RecyclerView.Adapter<ColoringBookListAdapter.ViewHolder>() {
    private lateinit var coloringBookList: List<ColoringBook>

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding: ItemColoringbookBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.item_coloringbook,
                parent, false)
        return ViewHolder(binding, mainVM)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(coloringBookList[position])
    }

    fun updateColoringBookList(coloringBookList: List<ColoringBook>){
        this.coloringBookList = coloringBookList
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int = if(::coloringBookList.isInitialized) coloringBookList.size else 0

    class ViewHolder(private val binding: ItemColoringbookBinding, private val mainVM:MainVM): RecyclerView.ViewHolder(binding.root){
        private val coloringVM = ColoringBookItemVM()

        fun bind(coloring: ColoringBook){
            coloringVM.bind(coloring.url)
            binding.viewModel = coloringVM
            binding.coloringImage.setOnClickListener {
                mainVM.imageUrl.value = coloring.url
            }
        }
    }
}

package com.hooitis.hoo.edgecoloringbook.ui

import android.databinding.DataBindingUtil
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.hooitis.hoo.edgecoloringbook.R
import com.hooitis.hoo.edgecoloringbook.databinding.ItemColorBinding
import com.hooitis.hoo.edgecoloringbook.databinding.ItemImageBinding
import com.hooitis.hoo.edgecoloringbook.model.quiz.Quiz
import com.hooitis.hoo.edgecoloringbook.vm.ColorItemVM
import com.hooitis.hoo.edgecoloringbook.vm.MainVM
import com.hooitis.hoo.edgecoloringbook.vm.QuizItemVM


class ColorListAdapter constructor(private val mainVM: MainVM): RecyclerView.Adapter<ColorListAdapter.ViewHolder>() {
    private lateinit var colorList: List<Int>

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding: ItemColorBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.item_color,
                parent, false)
        return ViewHolder(binding, mainVM)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(colorList[position])
    }

    fun updateColorList(colorList: List<Int>){
        this.colorList = colorList
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int = if(::colorList.isInitialized) colorList.size else 0

    class ViewHolder(private val binding: ItemColorBinding, private val mainVM: MainVM): RecyclerView.ViewHolder(binding.root){
        private val colorVM = ColorItemVM()

        fun bind(color: Int){
            colorVM.bind(color)
            binding.viewModel = colorVM
            binding.colorBtn.setOnClickListener {
                mainVM.brushColor.postValue(color)
            }
        }
    }
}

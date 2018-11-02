package com.hooitis.hoo.edgecoloringbook.ui

import android.databinding.DataBindingUtil
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.hooitis.hoo.edgecoloringbook.R
import com.hooitis.hoo.edgecoloringbook.databinding.ItemImageBinding
import com.hooitis.hoo.edgecoloringbook.model.quiz.Quiz
import com.hooitis.hoo.edgecoloringbook.vm.QuizItemVM


class QuizImageListAdapter: RecyclerView.Adapter<QuizImageListAdapter.ViewHolder>() {
    private lateinit var quizList: List<Quiz>

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding: ItemImageBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.item_image,
                parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(quizList[position])
    }

    fun updateQuizList(quizList: List<Quiz>){
        this.quizList = quizList
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int = if(::quizList.isInitialized) quizList.size else 0

    class ViewHolder(private val binding: ItemImageBinding): RecyclerView.ViewHolder(binding.root){
        private val quizVM = QuizItemVM()

        fun bind(quiz: Quiz){
            quizVM.bind(quiz)
            binding.viewModel = quizVM
        }
    }
}

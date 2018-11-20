package com.hooitis.hoo.edgecoloringbook.ui

import android.content.DialogInterface
import android.databinding.DataBindingUtil
import android.support.v7.app.AlertDialog
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import com.hooitis.hoo.edgecoloringbook.R
import com.hooitis.hoo.edgecoloringbook.databinding.ItemTempColoringbookBinding
import com.hooitis.hoo.edgecoloringbook.utils.UiUtils
import com.hooitis.hoo.edgecoloringbook.vm.MainVM
import com.hooitis.hoo.edgecoloringbook.vm.TempColoringBookItemVM


class TempColoringBookListAdapter constructor(private val mainVM:MainVM) : RecyclerView.Adapter<TempColoringBookListAdapter.ViewHolder>() {
    private lateinit var coloringBookList: List<Long>

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding: ItemTempColoringbookBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.item_temp_coloringbook,
                parent, false)
        return ViewHolder(binding, mainVM)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(coloringBookList[position])
    }

    fun updateTempColoringBookList(coloringBookList: List<Long>){
        this.coloringBookList = coloringBookList
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int = if(::coloringBookList.isInitialized) coloringBookList.size else 0

    class ViewHolder(private val binding: ItemTempColoringbookBinding, private val mainVM:MainVM): RecyclerView.ViewHolder(binding.root){
        private val coloringVM = TempColoringBookItemVM()

        fun bind(id: Long){
            binding.viewModel = coloringVM
            val tempColoringBook = mainVM.getTempColoringBook(id)
            binding.coloringImage.setImageBitmap(UiUtils.convertStringToBitmap(tempColoringBook.imageData))
            binding.coloringImage.setOnClickListener {
                mainVM.tempImageId.postValue(id)
            }
            binding.coloringImage.setOnLongClickListener {
                val dialog = AlertDialog.Builder(it.context)
                dialog.setTitle(R.string.do_you_want_to_remove)

                dialog.setPositiveButton(R.string.remove) { dialog, _ ->
                    mainVM.deleteTempColoringBook(id)
                    mainVM.getTempColoringBookList()
                    dialog.dismiss()

                }
                dialog.setNegativeButton(R.string.cancel) { dialog, _ ->
                    dialog.dismiss()
                }
                val alertDialog = dialog.create()
                alertDialog.show()

                return@setOnLongClickListener true
            }
        }
    }
}

package com.hooitis.hoo.edgecoloringbook.utils

import android.animation.ObjectAnimator
import android.arch.lifecycle.MutableLiveData
import android.databinding.BindingAdapter
import android.graphics.Bitmap
import android.media.Image
import android.support.constraint.ConstraintLayout
import android.support.design.widget.FloatingActionButton
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.View
import android.view.animation.AnimationUtils
import android.view.animation.TranslateAnimation
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.google.firebase.storage.FirebaseStorage
import com.hooitis.hoo.edgecoloringbook.R
import com.hooitis.hoo.edgecoloringbook.ui.paint.ColoringView
import com.hooitis.hoo.edgecoloringbook.ui.paint.ReviseDrawingView
import com.hooitis.hoo.edgecoloringbook.utils.extension.getParentActivity


@BindingAdapter("adapter")
@Suppress("unused")
fun setAdapter(view: RecyclerView, adapter: RecyclerView.Adapter<*>) {
    view.adapter = adapter
}

@BindingAdapter("itemPosition")
@Suppress("unused")
fun setItemPosition(view: RecyclerView, position: MutableLiveData<Int>?){
    Log.d("Speech", position!!.value.toString())

    if(position!!.value == 0) {
        view.smoothScrollToPosition(0)
        return
    }

    view.smoothScrollToPosition(position.value!!)
}

@BindingAdapter("android:text")
@Suppress("unused")
fun setText(view: TextView, data: MutableLiveData<Float>?){
    if (data != null)
        view.text = data.value.toString()
}

@BindingAdapter("imageRes")
@Suppress("unused")
fun setImageResource(view: ImageView, image_id: MutableLiveData<Int>?){
    if(image_id!!.value == 0){
        view.setImageResource(R.drawable.microphone)
        return
    }
    view.setImageResource(image_id.value!!)
}

@BindingAdapter("imageResUri")
@Suppress("unused")
fun setImageResourceByURI(view: ImageView, image_id: MutableLiveData<String>?){
    if(image_id!!.value.isNullOrEmpty()) {
        view.setImageResource(R.drawable.microphone)
        return
    }
    val imageUrl = image_id.value!!

    if(imageUrl.startsWith("http"))
        Glide.with(view.getParentActivity()!!)
                .load(imageUrl)
                .into(view)
    else
        Glide.with(view.getParentActivity()!!)
                .load(FirebaseStorage.getInstance().reference.child(imageUrl))
                .into(view)
}

@BindingAdapter("countText")
@Suppress("unused")
fun setCountText(view: TextView, count: MutableLiveData<String>){
    view.text = count.value

    val startSize = view.resources.getDimension(R.dimen.textTitleSize)
    val endSize = view.resources.getDimension(R.dimen.textMediumSize)

    val animator = ObjectAnimator.ofFloat(view, "textSize", startSize, endSize)
    ObjectAnimator.ofFloat()

    animator.duration = 1000
    animator.start()
}

@BindingAdapter("beforeCountText")
@Suppress("unused")
fun setBeforeCountText(view: TextView, count: MutableLiveData<String>){
    view.text = count.value
}

@BindingAdapter("scaleFactor")
@Suppress("unused")
fun setScaleFactor(view: ImageView, scale: MutableLiveData<Float>){
    view.scaleX = scale.value!!
    view.scaleY = scale.value!!
}

@BindingAdapter("scaleFactor")
@Suppress("unused")
fun setScaleFactor(view: ColoringView, scale: MutableLiveData<Float>){
    view.scaleX = scale.value!!
    view.scaleY = scale.value!!
//    view.brushScale(scale.value!!)
}

@BindingAdapter("scaleFactor")
@Suppress("unused")
fun setScaleFactor(view: ReviseDrawingView, scale: MutableLiveData<Float>){
    view.scaleX = scale.value!!
    view.scaleY = scale.value!!
    view.brushScale(scale.value!!)
}

@BindingAdapter("drawingMode")
@Suppress("unused")
fun setDrawingMode(view: ColoringView, drawingMode: MutableLiveData<Int>){
    view.setDrawingMode(drawingMode.value!!)
}

@BindingAdapter("drawingMode")
@Suppress("unused")
fun setDrawingMode(view: ReviseDrawingView, drawingMode: MutableLiveData<Int>){
    view.setDrawingMode(drawingMode.value!!)
}

@BindingAdapter("brushType")
@Suppress("unused")
fun setBrush(view: ColoringView, type: MutableLiveData<Int>){
    when(type.value){
        0 -> {
            view.changeToPencil()
        }
        else -> {
            view.changeToErase()
        }
    }
}

@BindingAdapter("brushType")
@Suppress("unused")
fun setBrush(view: ReviseDrawingView, type: MutableLiveData<Int>){
    when(type.value){
        0 -> {
            view.changeToPencil()
        }
        else -> {
            view.changeToErase()
        }
    }
}

@BindingAdapter("scaleFactor")
@Suppress("unused")
fun setScaleFactor(view: ConstraintLayout, scale: MutableLiveData<Float>){
    view.scaleX = scale.value!!
    view.scaleY = scale.value!!
}

@BindingAdapter("imageBitmap")
@Suppress("unused")
fun setBitmap(view: ImageView, url: MutableLiveData<String>){
    val imageUrl = url.value!!

    Log.d("ImageURL", imageUrl)

    if(imageUrl.startsWith("http"))
        Glide.with(view.getParentActivity()!!)
                .load(imageUrl)
                .into(view)
    else
        Glide.with(view.getParentActivity()!!)
                .load(FirebaseStorage.getInstance().reference.child(imageUrl))
                .into(view)
}

@BindingAdapter("floatingOpen")
@Suppress("unused")
fun setFloatingVisible(view: FloatingActionButton, visible: MutableLiveData<Boolean>){
    if(visible.value == null)
        return

    val isFabOpen = visible.value!!

    var anim = AnimationUtils.loadAnimation(view.context, R.anim.fab_open)

    if(isFabOpen)
        anim = AnimationUtils.loadAnimation(view.context, R.anim.fab_close)

    view.apply {
        startAnimation(anim)
        isClickable = !isFabOpen
    }

}
@BindingAdapter("visibility")
@Suppress("unused")
fun setVisibility(view: Button, visible: MutableLiveData<Boolean>){
    if(visible.value == null)
        return

    val isFabOpen = visible.value!!

    var anim = AnimationUtils.loadAnimation(view.context, R.anim.fab_open)

    if(!isFabOpen)
        anim = AnimationUtils.loadAnimation(view.context, R.anim.fab_close)

    view.apply {
        startAnimation(anim)
        isClickable = isFabOpen
    }

}

package com.hooitis.hoo.edgecoloringbook.utils

import android.app.Activity
import android.content.ContentResolver
import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.provider.MediaStore
import android.support.design.widget.Snackbar
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentActivity
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.ImageView
import android.widget.Toast
import com.bumptech.glide.Glide
import com.google.firebase.storage.FirebaseStorage
import java.io.ByteArrayOutputStream
import java.text.NumberFormat
import java.util.*

@Suppress("unused")
class UiUtils {
    companion object {
        fun addNewFragment(activity: FragmentActivity, fragment: Fragment, container_id:Int, addToBackStack: Boolean = true) = activity.supportFragmentManager.apply {
            this.beginTransaction()
                    .addToBackStack(null)
                    .replace(container_id, fragment, "")
                    .commit()
        }

        fun replaceNewFragment(activity:FragmentActivity, fragment: Fragment, container_id:Int) = activity.supportFragmentManager.apply {
            this.beginTransaction()
                    .replace(container_id, fragment, "")
                    .commit()
        }

        fun getFormatNumber(number: Int): String = NumberFormat.getNumberInstance(Locale.US).format(number)

        fun convertBMURI(contentResolver: ContentResolver, bitmap: Bitmap): Uri {
            val bytes = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes)
            val path = MediaStore.Images.Media.insertImage(contentResolver, bitmap, UUID.randomUUID().toString(), null)
            return Uri.parse(path)
        }

        fun convertURIBM(contentResolver: ContentResolver, uri: Uri): Bitmap = MediaStore.Images.Media.getBitmap(contentResolver, uri)
        fun makeSnackbar(view: View, resId: Int) = Snackbar.make(view, resId, Snackbar.LENGTH_SHORT).show()
        fun makeSnackbar(view: View, msg: String) = Snackbar.make(view, msg, Snackbar.LENGTH_SHORT).show()
        fun makeToast(view: View, resId: Int) = Toast.makeText(view.context, resId, Toast.LENGTH_SHORT).show()
        fun makeToast(view: View, msg: String) = Toast.makeText(view.context, msg, Toast.LENGTH_SHORT).show()
        fun loadImage(view: ImageView, url: String) = Glide.with(view.rootView)
                .load(FirebaseStorage.getInstance().reference.child(url))
                .into(view)
        fun hideSoftKeyboard(activity: Activity)  = (activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager).hideSoftInputFromWindow(activity.currentFocus.windowToken, 0)
        fun hideSoftKeyboard(view: View, context: Context) =  (context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager).hideSoftInputFromWindow(view.windowToken, 0)
    }
}

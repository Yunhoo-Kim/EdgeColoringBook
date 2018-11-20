package com.hooitis.hoo.edgecoloringbook.utils

import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.ColorMatrix
import android.util.Log
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.ByteArrayOutputStream
import java.util.*
import kotlin.math.atan2
import kotlin.math.floor
import kotlin.math.pow
import kotlin.math.sqrt

@Suppress("unused")
class EdgeDetection {

        private fun cannyBitmap(bitmap: Bitmap, xMatrix: Array<IntArray>, yMatrix: Array<IntArray>): Bitmap {
            val width = bitmap.width
            val height = bitmap.height
            val resultBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)

            for (i in 1 until width) {
                for (j in 1 until height) {
                    var xRed = 0
                    var yRed = 0

                    for (k in -1..1)
                        for (l in -1..1) {
                            val x = i + k
                            val y = j + l
                            if (x < 0 || y < 0 || x >= width || y >= height)
                                continue

                            val cPixel = bitmap.getPixel(x, y)
                            val cRed = Color.red(cPixel)

                            xRed += cRed * xMatrix[k + 1][l + 1]
                            yRed += cRed * yMatrix[k + 1][l + 1]
                        }

                    val red:Int = sqrt((xRed * xRed + yRed * yRed).toFloat()).run{
                        if(this > THRESHOLD){
                            return@run 0
                        }else
                            return@run 255
                    }

                    resultBitmap.setPixel(i, j, Color.argb(255, red, red, red))
                }
            }
            return resultBitmap
        }
        fun cannyBitmap(bitmap: Bitmap, xMatrix: Array<FloatArray>, yMatrix: Array<FloatArray>): Bitmap {
            val width = bitmap.width
            val height = bitmap.height
            val resultBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)

            for (i in 1 until width) {
                for (j in 1 until height) {
                    var xP = 0f
                    var xRed = 0f

                    var yP = 0f
                    var yRed = 0f

                    for (k in -1..1)
                        for (l in -1..1) {
                            val x = i + k
                            val y = j + l
                            if (x < 0 || y < 0 || x >= width || y >= height)
                                continue

                            val cPixel = bitmap.getPixel(x, y)
                            val cRed = Color.red(cPixel)

                            xRed += cRed * xMatrix[k + 1][l + 1]
                            yRed += cRed * yMatrix[k + 1][l + 1]
                        }

                    val red:Int = sqrt(xRed.pow(2) + yRed.pow(2)).run{
                        if(this > THRESHOLD){
                            return@run 0
                        }else
                            return@run 255
                    }
                    resultBitmap.setPixel(i, j, Color.argb(255, red, red, red))
                }
            }
            return resultBitmap
        }

        private fun convertToGray(bitmap: Bitmap): Bitmap{
            val width = bitmap.width
            val height = bitmap.height
            val resultBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)

            for(i in 0 until width) {
                for (j in 0 until height) {
                    val cPixel = bitmap.getPixel(i, j)
                    val red = Color.red(cPixel)
                    val green = Color.green(cPixel)
                    val blue = Color.blue(cPixel)
                    val gray = (red * 0.3 + green * 0.59 + blue * 0.11).toInt()
                    resultBitmap.setPixel(i, j, Color.argb(255, gray, gray, gray))
                }
            }

            return resultBitmap
        }

        private fun convolutionBitmap(bitmap: Bitmap, matrix: Array<FloatArray>, num: Int): Bitmap{
            val width = bitmap.width
            val height = bitmap.height
            val resultBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
            val hNum :Int = floor(num.toFloat() / 2).toInt()

            Log.d("EdgeDetection", "$width $height $hNum")
            for(i in 1 until width) {
                for (j in 1 until height) {
                    var red = 0f

                    for(k in -hNum..hNum)
                        for(l in -hNum..hNum){
                            val x = i + k
                            val y = j + l
                            if(x < 0 || y < 0 || x >= width || y >= height)
                                continue

                            val cPixel = bitmap.getPixel(x, y)
                            val cRed = Color.red(cPixel)

                            red += cRed*matrix[k + hNum][l + hNum]
                        }

                    val color:Int = red.toInt().run{
                        if(this > THRESHOLD){
                            return@run 0
                        }else
                            return@run 255
                    }
                    resultBitmap.setPixel(i, j, Color.argb(255, color, color, color))
                }
            }

            return resultBitmap
        }

        fun truncate(num: Int): Int{
            return when {
                num < 0 -> 0
                num > 255 -> 255
                else -> num
            }
        }

        fun edgeDetection(bitmap: Bitmap): Bitmap{

            var width: Int = bitmap.width
            var height: Int = bitmap.height
            var fWidth: Double = bitmap.width.toDouble()
            var fHeight: Double = bitmap.height.toDouble()

            if((width > 700) or (height > 600)){
                if (width > height) {
                    val ratio = fHeight / fWidth
                    fWidth = 700.0
                    fHeight = fWidth * ratio
                } else {
                    val ratio = fWidth/ fHeight
                    fHeight = 600.0
                    fWidth = fHeight * ratio
                }
                width = fWidth.toInt()
                height = fHeight.toInt()
            }

            val newBitmap = Bitmap.createScaledBitmap(bitmap, width, height, false).run {
                val bytes = ByteArrayOutputStream()
                this.compress(Bitmap.CompressFormat.JPEG, 80, bytes)
                return@run convertToGray(this)
            }


            val xMatrix = arrayOf(
                    intArrayOf(1, 0, -1),
                    intArrayOf(2, 0, -2),
                    intArrayOf(1, 0, -1))

            val yMatrix = arrayOf(
                    intArrayOf(1, 2, 1),
                    intArrayOf(0, 0, 0),
                    intArrayOf(-1, -2, -1))

            return cannyBitmap(newBitmap, xMatrix, yMatrix)
        }

    fun makeRandomString(): String = UUID.randomUUID().toString()

    fun findEdge(rowShift: Int, colShift: Int, row: Int, col: Int, dir: Int, lowerThreshold: Int, width: Int, height: Int, edgeArray:Array<IntArray>){
//            var newRow: Int = 0
//            var newCol: Int = 0
//            var edgeEnd: Boolean = false
//
//            if(colShift < 0){
//                if(col > 0)
//                    newCol = col + colShift
//                else
//                    edgeEnd = true
//            }else if(col < width - 1){
//                newCol = col + colShift
//            }else
//                edgeEnd = true
//
//            if(rowShift < 0){
//                if(row > 0)
//                    newRow = row + rowShift
//                else
//                    edgeEnd = true
//            }else if(row < height -1){
//                newRow = row + rowShift
//            }else{
//                edgeEnd = true
//            }

//            while((edgeArray[newRow][newCol] == dir) && !edgeEnd){
//                inewRow * 3 * width + 3 * newCol
//            }

    }
    fun convertString2List(value: String): List<String> = Gson().fromJson(value, object : TypeToken<List<String>>() {}.type)
}

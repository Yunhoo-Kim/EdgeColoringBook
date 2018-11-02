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
class Utils {
    companion object {
        fun makeCalendarToDate(calendar: Calendar): Date{
            calendar.set(Calendar.HOUR_OF_DAY, 0)
            calendar.set(Calendar.MINUTE, 0)
            calendar.set(Calendar.SECOND, 0)
            calendar.set(Calendar.MILLISECOND, 0)
            return calendar.time
        }

        fun uploadImageToGCS(bitmap: Bitmap, storageReference: StorageReference): UploadTask {
            val fileName = "${Utils.makeRandomString()}.png"
            val imageRef = storageReference.child(fileName)
            val baos = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)

            return imageRef.putBytes(baos.toByteArray())
        }

        fun cannyBitmap(bitmap: Bitmap, xMatrix: Array<IntArray>, yMatrix: Array<IntArray>): Bitmap {
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
//                    var xGreen = 0f
//                    var xBlue = 0f

                    var yP = 0f
                    var yRed = 0f
//                    var yGreen = 0f
//                    var yBlue = 0f

                    for (k in -1..1)
                        for (l in -1..1) {
                            val x = i + k
                            val y = j + l
                            if (x < 0 || y < 0 || x >= width || y >= height)
                                continue

                            val cPixel = bitmap.getPixel(x, y)
                            val cRed = Color.red(cPixel)
//                            val cGreen = Color.green(cPixel)
//                            val cBlue = Color.blue(cPixel)

                            xRed += cRed * xMatrix[k + 1][l + 1]
//                            xGreen += cGreen * xMatrix[k + 1][l + 1]
//                            xBlue += cBlue * xMatrix[k + 1][l + 1]

                            yRed += cRed * yMatrix[k + 1][l + 1]
//                            yGreen += cGreen * yMatrix[k + 1][l + 1]
//                            yBlue += cBlue * yMatrix[k + 1][l + 1]
                        }

//                    val newColor = sqrt(xP.pow(2) + yP.pow(2)).toInt().run{
//                        if(this < 60){
//                            return@run 0
//                        }else
//                            return@run this
//                    }
                    val red:Int = sqrt(xRed.pow(2) + yRed.pow(2)).run{
                        if(this > THRESHOLD){
                            return@run 0
                        }else
                            return@run 255
                    }
//                    val blue = sqrt(xBlue.pow(2) + yBlue.pow(2)).run{
//                        if(this < THRESHOLD){
//                            return@run 0f
//                        }else
//                            return@run this
//                    }
//                    val green = sqrt(xGreen.pow(2) + yGreen.pow(2)).run{
//                        if(this < THRESHOLD){
//                            return@run 0f
//                        }else
//                            return@run this
//                    }
//
//                    val gray = (red * 0.3 + green * 0.59 + blue * 0.11).toInt().run {
//                        if(this <= 128){
//                            return@run 255
//                        }else
//                            return@run 0
//                    }

//                    edgeArray[i][j] = ((atan2(xRed, yRed) / 3.14) * 180.0).run {
//                        if((this < 22.5 && this > -22.5) || (this > 157.5 || this < -157.5))
//                            return@run 0
//                        else if((this < 22.5 && this > -22.5) || (this > 157.5 || this < -157.5))
//                            return@run 45
//                        else if((this < 22.5 && this > -22.5) || (this > 157.5 || this < -157.5))
//                            return@run 90
//                        else if((this < 22.5 && this > -22.5) || (this > 157.5 || this < -157.5))
//                            return@run 135
//                        else
//                            return@run 0
//                    }
//                    val color = truncate(red)
                    resultBitmap.setPixel(i, j, Color.argb(255, red, red, red))
                }
            }
            return resultBitmap
        }

        fun convertToGray(bitmap: Bitmap): Bitmap{
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

        fun convolutionBitmap(bitmap: Bitmap, matrix: Array<FloatArray>, num: Int): Bitmap{
            val width = bitmap.width
            val height = bitmap.height
            val resultBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
            val hNum :Int = floor(num.toFloat() / 2).toInt()

            Log.d("EdgeDetection", "$width $height $hNum")
            for(i in 1 until width) {
                for (j in 1 until height) {
                    var red = 0f
//                    var green = 0f
//                    var blue = 0f

                    for(k in -hNum..hNum)
                        for(l in -hNum..hNum){
                            val x = i + k
                            val y = j + l
                            if(x < 0 || y < 0 || x >= width || y >= height)
                                continue

                            val cPixel = bitmap.getPixel(x, y)
                            val cRed = Color.red(cPixel)
//                            val cGreen = Color.green(cPixel)
//                            val cBlue = Color.blue(cPixel)

                            red += cRed*matrix[k + hNum][l + hNum]
//                            green += cGreen*matrix[k + hNum][l + hNum]
//                            blue += cBlue*matrix[k + hNum][l + hNum]
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
            val bytes = ByteArrayOutputStream()
//            bitmap.compress(Bitmap.CompressFormat.JPEG, 60, bytes)

            var width: Int = bitmap.width
            var height: Int = bitmap.height

            if(bitmap.width > 1000 || bitmap.height > 800) {
                if ((bitmap.width.toFloat() / bitmap.height.toFloat()) > 1) {
                    width = (bitmap.width * 0.5).toInt()
                    height = (width * (bitmap.height.toFloat() / bitmap.width.toFloat())).toInt()
                } else {
                    height = (bitmap.height * 0.5).toInt()
                    width = (height * (bitmap.width.toFloat() / bitmap.height.toFloat())).toInt()
                }
            }

            val newBitmap = Bitmap.createScaledBitmap(bitmap, width, height, false).run {
                return@run convertToGray(this)
            }
//            var newBitmap = convertToGray(bitmap)

//            val gMatrix = arrayOf(
//                    floatArrayOf(2/159f, 4/159f, 5/159f, 4/159f, 2/159f),
//                    floatArrayOf(4/159f, 9/159f, 12/159f, 9/159f, 4/159f),
//                    floatArrayOf(5/159f, 12/159f, 15/159f, 12/159f, 5/159f),
//                    floatArrayOf(4/159f, 9/159f, 12/159f, 9/159f, 4/159f),
//                    floatArrayOf(2/159f, 4/159f, 5/159f, 4/159f, 2/159f))

            val gMatrix = arrayOf(
                    floatArrayOf(1/16f, 1/8f, 1/16f),
                    floatArrayOf(1/8f, 1/4f, 1/8f),
                    floatArrayOf(1/16f, 1/8f, 1/16f))

            val gdMatrix = arrayOf(
                    floatArrayOf(-1f, 1f, 1f),
                    floatArrayOf(-1f, -2f, 1f),
                    floatArrayOf(-1f, 1f, 1f))

            val xMatrix = arrayOf(
                    intArrayOf(1, 0, -1),
                    intArrayOf(2, 0, -2),
                    intArrayOf(1, 0, -1))

            val yMatrix = arrayOf(
                    intArrayOf(1, 2, 1),
                    intArrayOf(0, 0, 0),
                    intArrayOf(-1, -2, -1))

//            val xMatrix = arrayOf(
//                    floatArrayOf(1f, 0f, -1f),
//                    floatArrayOf(2f, 0f, -2f),
//                    floatArrayOf(1f, 0f, -1f))
//
//            val yMatrix = arrayOf(
//                    floatArrayOf(1f, 2f, 1f),
//                    floatArrayOf(0f, 0f, 0f),
//                    floatArrayOf(-1f, -2f, -1f))

            Log.d("EdgeDetection", "Gaussian start")
//            val gBitmap = Utils.convolutionBitmap(newBitmap, gMatrix, gMatrix.size)
            Log.d("EdgeDetection", "Gaussian end")
//            return newBitmap
//            return Utils.convolutionBitmap(newBitmap, gdMatrix, gdMatrix.size)
//            return Utils.convolutionBitmap(Utils.cannyBitmap(newBitmap, xMatrix, yMatrix), gdMatrix, gdMatrix.size)
            return Utils.cannyBitmap(newBitmap, xMatrix, yMatrix)
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
}

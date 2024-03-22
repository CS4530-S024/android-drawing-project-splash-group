package com.example.drawing_prototype

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel


// VM contains function to setup the bitmap, store change the pen's size, color, and shape,
// And draw color on the drawing board touch location
// Created by Chengyu Yang, Jiahua Zhao, Yitong Lu
class DrawingBoardModel: ViewModel() {
    // MutableLiveData for observer changes on current bitmap
    lateinit var bitmap: MutableLiveData<Bitmap>

    private var initialize: Int = 0
    // Initial pen
    val paint = Paint()
    var size_of_paint: Float = 20.0f
    var paint_type = "circle"
    // Canvas for the bitmap, use for drawing
    private lateinit var bitmapCanvas: Canvas

    // Initialize the model, create a square bimap and set default value for pen color and size
    fun initializeModel(width : Int, height : Int){
        paint.color = Color.BLACK
        // Create a new square bitmap with width 1024
        bitmap = MutableLiveData(android.graphics.Bitmap.createBitmap(1100, 1100,
            android.graphics.Bitmap.Config.ARGB_8888))
        bitmapCanvas = Canvas(bitmap.value!!)
        // Set the background color to white
        bitmapCanvas.drawColor(Color.WHITE)
        size_of_paint = 20.0f
        initialize = 1
    }

    // Update pen type
    fun updateType(string: String){
        paint_type = string
    }

    // Update pen color
    fun updatePaintColor(color: Int){
        paint.color = color
    }

    // Update pen size
    fun updateSizeOfPaint(size: Float){
        size_of_paint = size
    }

    fun isInitialize(): Int {
        return initialize
    }

    //draws to our bitmap.
    fun draw(x: Float, y: Float){
        when(paint_type){

            "circle" -> drawCircle(x, y)
            "rectangle" -> drawRectangle(x, y)

        }
    }

    // Draw a circle on the bitmap at input location
    fun drawCircle(x: Float, y: Float){

        bitmapCanvas.drawCircle(x, y, size_of_paint, paint)
        bitmap.value = bitmap.value

    }

    // Draw a rectangle on the bitmap at input location
    fun  drawRectangle(x: Float, y: Float){

        bitmapCanvas.drawRect(x-size_of_paint, y+size_of_paint,
            x+size_of_paint, y-size_of_paint, paint )
        bitmap.value = bitmap.value

    }
}
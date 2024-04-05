package com.example.drawing_prototype

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Path
import android.graphics.Rect
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View

// A drawing board class contains function to display and draw on a bitmap
// Created by Chengyu Yang, Jiahua Zhao, Yitong Lu
class DrawingBoardView(context: Context, attrs: AttributeSet) : View(context, attrs) {

    private lateinit var bitmap: Bitmap
    private lateinit var paint: Paint
    private var lastX = 0f
    private var lastY = 0f

    private val rect: Rect by lazy { Rect(6,4,width, height) }

    // When it initialize, setup touch handler
    init {
        setupTouchHandler()
    }


    fun setupTouchHandler() {
        setOnTouchListener { v, event ->
            val x = event.x
            val y = event.y

            when (event.action) {
                MotionEvent.ACTION_DOWN -> {

                    lastX = x
                    lastY = y
                    v.performClick()
                    true
                }
                MotionEvent.ACTION_MOVE -> {
                    // Interpolate between the last and current points
                    interpolateAndDraw(lastX, lastY, x, y)
                    lastX = x
                    lastY = y
                    v.performClick()
                    true
                }
                else -> false
            }
        }
    }

    private fun interpolateAndDraw(fromX: Float, fromY: Float, toX: Float, toY: Float) {
        // Pseudo-code to interpolate between points and draw
        // This should actually draw on a bitmap or a canvas that you manage.
        val path = Path()
        path.moveTo(fromX, fromY)
        path.lineTo(toX, toY)
        // Use your paint and canvas to draw the path
        // e.g., canvas.drawPath(path, paint)
        (context as? MainActivity)?.drawingBoardModel?.draw(path) // Adjust this method to your actual drawing method
    }



    // Set up the drawing board
    // Initialize the drawing board with bitmap and paint.
    fun initializeDrawingBoard(bitmap : Bitmap, paint: Paint){
        this.bitmap = bitmap
        this.paint = paint
    }

    // Update the bitmap displayed on the drawing board
    fun updateBitmap(newBitmap: Bitmap){
        bitmap = newBitmap
        invalidate()
    }

    // Draws the bitmap onto the canvas within the specified rect.
    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        canvas.drawBitmap(bitmap, null, rect, paint)
    }
}